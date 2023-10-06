package com.lxl.business.service.impl;

import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxl.business.domain.DailyTrainCarriage;
import com.lxl.business.domain.DailyTrainSeat;
import com.lxl.business.domain.DailyTrainTicket;
import com.lxl.business.enums.ConfirmOrderStatusTypeEnum;
import com.lxl.business.enums.SeatColTypeEnum;
import com.lxl.business.enums.SeatTypeEnum;
import com.lxl.business.mapper.DailyTrainCarriageMapper;
import com.lxl.business.mapper.DailyTrainSeatMapper;
import com.lxl.business.mapper.DailyTrainTicketMapper;
import com.lxl.business.req.ConfirmOrderTicketReq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.ConfirmOrder;
import com.lxl.business.mapper.ConfirmOrderMapper;
import com.lxl.business.req.ConfirmOrderDoReq;
import com.lxl.business.req.ConfirmOrderQueryReq;
import com.lxl.business.resp.ConfirmOrderQueryResp;
import com.lxl.business.service.ConfirmOrderService;
import com.lxl.common.context.MemberInfoContext;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.resp.PageResp;
import com.lxl.common.utils.SnowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author 13430
 * @description 针对表【confirm_order(确认订单)】的数据库操作Service实现
 * @createDate 2023-10-05 11:47:00
 */
@Slf4j
@Service
public class ConfirmOrderServiceImpl implements ConfirmOrderService {
    @Autowired
    ConfirmOrderMapper confirmOrderMapper;
    @Autowired
    DailyTrainTicketMapper dailyTrainTicketMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    DailyTrainCarriageMapper dailyTrainCarriageMapper;
    @Autowired
    DailyTrainSeatMapper dailyTrainSeatMapper;
    @Autowired
    ConfirmOrderAfterService confirmOrderAfterService;


    @Override
    public PageResp<ConfirmOrderQueryResp> queryList(ConfirmOrderQueryReq req) {
        LambdaQueryWrapper<ConfirmOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ConfirmOrder::getMemberId);

        if (!ObjectUtils.isEmpty(req.getPageSize()) && !ObjectUtils.isEmpty(req.getCurrentPage())) {
            log.info("当前页码：{}", req.getCurrentPage());
            log.info("当前页面大小：{}", req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(), req.getPageSize());
        }
        List<ConfirmOrder> confirmOrders = confirmOrderMapper.selectList(wrapper);
        PageInfo<ConfirmOrder> confirmOrderPageInfo = new PageInfo<>(confirmOrders);
        int pages = confirmOrderPageInfo.getPages();
        long total = confirmOrderPageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<ConfirmOrderQueryResp> list = new ArrayList<>();
        confirmOrders.forEach(confirmOrder -> {
            ConfirmOrderQueryResp queryResp = new ConfirmOrderQueryResp();
            BeanUtils.copyProperties(confirmOrder, queryResp);
            list.add(queryResp);
        });
        PageResp<ConfirmOrderQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return pageResp;
    }

    @Override
    public void doConfirm(ConfirmOrderDoReq req) {
        Date now = new Date(System.currentTimeMillis());
        // 车次是否存在、余票是否存在、车次是否在有效期之内 ticket条数大于0 同乘客同车次是否已经买过
        //保存确认订单表，初始化
        //查出余票初始化
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowUtils.nextSnowId());
        confirmOrder.setMemberId(MemberInfoContext.getMemberId());
        confirmOrder.setDate(req.getDate());
        confirmOrder.setTrainCode(req.getTrainCode());
        confirmOrder.setStart(req.getStart());
        confirmOrder.setEnd(req.getEnd());
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        try {
            confirmOrder.setTickets(objectMapper.writeValueAsString(req.getTickets()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        confirmOrder.setStatus(ConfirmOrderStatusTypeEnum.INIT.getCode());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);

        confirmOrderMapper.insert(confirmOrder);

        DailyTrainTicket dailyTrainTicket = dailyTrainTicketMapper.selectById(confirmOrder.getDailyTrainTicketId());
        if (ObjectUtils.isEmpty(dailyTrainTicket)) {
            throw new BusinessException(BussinessExceptionEnum.NO_DAILY_TRAIN_TICKET_INFO);
        }
        log.info("查出余票记录{}", dailyTrainTicket.toString());

        //判断余票的数量
        reduceTickets(req, dailyTrainTicket);
        Long dailyTrainId = dailyTrainTicket.getDailyTrainId();
        //计算ticket选座的偏移值
        List<ConfirmOrderTicketReq> tickets = req.getTickets();
        ConfirmOrderTicketReq ticketReq0 = tickets.get(0);
        List<DailyTrainSeat> res = new ArrayList<>();
        if (CollUtil.isNotEmpty(tickets) && StringUtils.hasText(ticketReq0.getSeat())) {
            log.info("会员有进行选座");
            //构建选座
            List<SeatColTypeEnum> seatCols = SeatColTypeEnum.getSeatCols(ticketReq0.getSeatType());
            List<String> referSeatList = new ArrayList<>();
            for (int i = 1; i <= 2; i++) {
                for (SeatColTypeEnum seatCol : seatCols) {
                    referSeatList.add(seatCol.code + String.valueOf(i));
                }
            }
            //先获取绝对的偏移量
            List<Integer> offsetList = tickets.stream().map(confirmOrderTicketReq -> referSeatList.indexOf(confirmOrderTicketReq.getSeat())).toList();
            //再将所有元素减去第一个座位的绝对偏移量
            Integer firstAbsoluteOffset = offsetList.get(0);
            offsetList = offsetList.stream().map(offset -> offset - firstAbsoluteOffset).toList();
            log.info("所有座位相对于第一个座位的偏移量{}", offsetList.toString());

            getSeat(res,
                    dailyTrainId,
                    ticketReq0.getSeatType(),
                    dailyTrainTicket.getStartIndex(),
                    dailyTrainTicket.getEndIndex(),
                    ticketReq0.getSeat().substring(0, ticketReq0.getSeat().length() - 1),
                    offsetList);
        } else {
            log.info("会员没有进行选座");
            for (ConfirmOrderTicketReq ticket : tickets) {
                getSeat(res,
                        dailyTrainId,
                        ticket.getSeatType(),
                        dailyTrainTicket.getStartIndex(),
                        dailyTrainTicket.getEndIndex(),
                        null,
                        null);
            }
        }
        log.info("选座完成，被选择的座位:{}",res);

        if (CollUtil.isNotEmpty(res)) {
            confirmOrderAfterService.updateSellById(res);
        }

        //选座
        //挑选符合条件的座位,如果该车厢不满足条件则进入下一个车厢(多个选座应该在同一个车厢)
        //选中座位之后的处理
        //修改座位表的情况
        //余票详情表的余票
        //添加会员购买记录
    }

    /**
     * 一个车厢一个车厢的查找
     */
    private void getSeat(List<DailyTrainSeat> res, Long dailyTrainId, String seatTypeCode, Integer startIndex, Integer endIndex, String seatCol, List<Integer> offsetList) {
        //避免选择已经选择了的座位
        HashSet<Long> set = new HashSet<>();
        res.forEach(seat->set.add(seat.getId()));
        //一个车厢一个车厢的查找
        LambdaQueryWrapper<DailyTrainCarriage> dailyTrainCarriageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dailyTrainCarriageLambdaQueryWrapper.orderByAsc(DailyTrainCarriage::getTrainIndex);
        dailyTrainCarriageLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainId), DailyTrainCarriage::getDailyTrainId, dailyTrainId);
        List<DailyTrainCarriage> dailyTrainCarriages = dailyTrainCarriageMapper.selectList(dailyTrainCarriageLambdaQueryWrapper);
        log.info("查找到的车厢数目为" + dailyTrainCarriages.size());
        //遍历每一节车厢,查找车座
        for (DailyTrainCarriage carriage : dailyTrainCarriages) {
            List<DailyTrainSeat> tempList = new ArrayList<>();
            if (!carriage.getSeatType().equals(seatTypeCode)){
                log.info("座位类型与车位类型不匹配,当前车厢座位类型：{},目标的席位类型：{}",carriage.getSeatType(),seatTypeCode);
                continue;
            }
            //找出所有的座位
            Long dailyCarriageId = carriage.getId();
            LambdaQueryWrapper<DailyTrainSeat> dailyTrainSeatLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dailyTrainSeatLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyCarriageId), DailyTrainSeat::getCarriageId, dailyCarriageId);
            dailyTrainSeatLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainId), DailyTrainSeat::getDailyTrainId, dailyTrainId);
            dailyTrainSeatLambdaQueryWrapper.orderByAsc(DailyTrainSeat::getCarriageSeatIndex);//按照座位序号升序排列
            List<DailyTrainSeat> seatList = dailyTrainSeatMapper.selectList(dailyTrainSeatLambdaQueryWrapper);
            log.info("查找到的车厢{}的座位数为" + seatList.size(), carriage.getTrainIndex());


            for (int i = 0; i < seatList.size(); i++) {
                DailyTrainSeat dailyTrainSeat = seatList.get(i);

                if (!StringUtils.hasText(seatCol)) {
                    log.info("没有进行选座");
                } else {
                    log.info("进行了选座");
                    if (!seatCol.equals(dailyTrainSeat.getSeatCol())) {
                        log.info("座位{}的COL不对，将查找下一个座位，目标列{},当前列{}"
                                , dailyTrainSeat.getCarriageSeatIndex()
                                , seatCol, dailyTrainSeat.getSeatCol());
                        continue;
                    }
                }


                DailyTrainSeat checkedSeat = BeanUtil.copyProperties(dailyTrainSeat, DailyTrainSeat.class);
                if (!set.contains(checkedSeat.getId())&&canSell(checkedSeat, startIndex, endIndex)) {
                    log.info("座位{}被选中",dailyTrainSeat.getCarriageSeatIndex());
                    tempList.add(checkedSeat);
                } else {
                    log.info("没有选中座位{}",dailyTrainSeat.getCarriageSeatIndex());
                    continue;
                }

                boolean isGetAllOffsetSeat = true;
                if (CollUtil.isNotEmpty(offsetList)) {
                    log.info("偏移值有{}可选",offsetList);
                    for (int j = 1; j < offsetList.size(); j++) {
                        int nextIndex = offsetList.get(j) + i;

                        if (nextIndex>=seatList.size()){
                            log.info("下一个偏移量指向了另一节车厢，查找失败");
                            isGetAllOffsetSeat = false;
                            break;
                        }
                        DailyTrainSeat nextDailyTrainSeat = BeanUtil.copyProperties(seatList.get(nextIndex),DailyTrainSeat.class);
                        if (!set.contains(nextDailyTrainSeat.getId())&&canSell(nextDailyTrainSeat,startIndex,endIndex)){
                            log.info("座位{}被选中",nextDailyTrainSeat.getCarriageSeatIndex());
                            tempList.add(nextDailyTrainSeat);
                        }else {
                            log.info("没有选中座位{}",nextDailyTrainSeat.getCarriageSeatIndex());
                            isGetAllOffsetSeat = false;
                          break ;
                        }
                    }
                }
                if (!isGetAllOffsetSeat) {
                    tempList = new ArrayList<>();
                    continue;
                }

                res.addAll(tempList);
                return;
            }
        }
    }

    private boolean canSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex) {
        String sell = dailyTrainSeat.getSell();
        String part = sell.substring(startIndex, endIndex);
        if (Integer.parseInt(part) > 0) {
            log.info("该座位已经被购买");
            return false;
        } else {
            char[] charArray = sell.toCharArray();
            Arrays.fill(charArray, startIndex, endIndex, '1');
            dailyTrainSeat.setSell(String.valueOf(charArray));
            log.info("该座位可以被购买，并且sell属性已被更新,更新前{}，更新后{}", sell, dailyTrainSeat.getSell());
            return true;
        }
    }

    private static void reduceTickets(ConfirmOrderDoReq req, DailyTrainTicket dailyTrainTicket) {
        List<ConfirmOrderTicketReq> tickets = req.getTickets();
        //预扣减余票数量,并且判断余票是否不足
        for (ConfirmOrderTicketReq ticket : tickets) {
            SeatTypeEnum seatTypeEnum = Arrays.stream(SeatTypeEnum.values()).filter(item -> Objects.equals(item.code, ticket.getSeatType())).findFirst().orElse(null);
            if (seatTypeEnum != null) {
                switch (seatTypeEnum) {
                    case YDZ -> {
                        int countLeft = dailyTrainTicket.getYdz() - 1;
                        if (countLeft < 0) {
                            throw new BusinessException(BussinessExceptionEnum.TICKET_INSUFFICIENT_ERROR);
                        }
                        dailyTrainTicket.setYdz(countLeft);
                    }
                    case EDZ -> {
                        int countLeft = dailyTrainTicket.getEdz() - 1;
                        if (countLeft < 0) {
                            throw new BusinessException(BussinessExceptionEnum.TICKET_INSUFFICIENT_ERROR);
                        }
                        dailyTrainTicket.setEdz(countLeft);
                    }
                    case YW -> {
                        int countLeft = dailyTrainTicket.getYw() - 1;
                        if (countLeft < 0) {
                            throw new BusinessException(BussinessExceptionEnum.TICKET_INSUFFICIENT_ERROR);
                        }
                        dailyTrainTicket.setYw(countLeft);
                    }
                    case RW -> {
                        int countLeft = dailyTrainTicket.getRw() - 1;
                        if (countLeft < 0) {
                            throw new BusinessException(BussinessExceptionEnum.TICKET_INSUFFICIENT_ERROR);
                        }
                        dailyTrainTicket.setRw(countLeft);
                    }
                }
            } else {
                throw new BusinessException(BussinessExceptionEnum.WRONG_ENUM_CODE);
            }
        }
    }

}




