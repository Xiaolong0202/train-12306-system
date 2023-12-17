package com.lxl.business.service.impl;

import java.util.*;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxl.business.domain.*;
import com.lxl.business.dto.ConfirmOrderMQDTO;
import com.lxl.business.enums.ConfirmOrderStatusTypeEnum;
import com.lxl.business.enums.SeatColTypeEnum;
import com.lxl.business.enums.SeatTypeEnum;
import com.lxl.business.mapper.*;
import com.lxl.business.req.ConfirmOrderTicketReq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.req.ConfirmOrderQueryReq;
import com.lxl.business.req.DailyTrainTicketQueryReq;
import com.lxl.business.req.WebDailyTrainTicketQueryReq;
import com.lxl.business.resp.ConfirmOrderQueryResp;
import com.lxl.business.service.ConfirmOrderService;
import com.lxl.business.service.DailyTrainTicketService;
import com.lxl.common.constant.RedisKeyPrefix;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.resp.PageResp;
import com.lxl.common.utils.SnowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
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
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    TrainTokenMapper trainTokenMapper;
    @Autowired
    DailyTrainTicketService dailyTrainTicketService;

    public static final int tryTimes = 3;//设置重试获取锁的次数

    //    private static final Lock lock = new ReentrantLock();


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
    public Integer queryOrderQueueStatus(Long confirmOrderId) {
        ConfirmOrder confirmOrder = confirmOrderMapper.selectById(confirmOrderId);
        if (ObjectUtil.isNotEmpty(confirmOrder)) {
            String statusDB = confirmOrder.getStatus();
            ConfirmOrderStatusTypeEnum typeEnum = EnumUtil.getBy(ConfirmOrderStatusTypeEnum::getCode, statusDB);
            int result = switch (typeEnum) {
                case INIT -> 999;
                case SUCCESS -> -1;
                case FAILURE -> -2;
                case EMPTY -> -3;
                case CANCEL -> -4;
                case PENDING -> 0;
            };
            if (result == 999) {
                //表示还没被处理说明需要查询排队
                result = confirmOrderMapper.queryOrderQueueStatus(confirmOrder, ConfirmOrderStatusTypeEnum.INIT.getCode(), ConfirmOrderStatusTypeEnum.PENDING.getCode());
            }
            return result;
        }
        throw new BusinessException(BussinessExceptionEnum.NO_TRAIN_INFO);
    }

    @Override
    public Integer cancel(Long confirmOrderId) {
        LambdaQueryWrapper<ConfirmOrder> confirmOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        confirmOrderLambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(confirmOrderId),ConfirmOrder::getId,confirmOrderId);
        confirmOrderLambdaQueryWrapper.eq(ConfirmOrder::getStatus,ConfirmOrderStatusTypeEnum.INIT.getCode());
        //找到所有的正在初始化的订单并将订单的状态设置为取消
       return confirmOrderMapper.update(ConfirmOrder.builder().status(ConfirmOrderStatusTypeEnum.CANCEL.getCode()).build(),
                confirmOrderLambdaQueryWrapper);
    }

    @Override
    public void doConfirm(ConfirmOrderMQDTO mqdto) {

//        lock.lock();//加锁
        //使用redis加上分布式锁
        String lockKey = RedisKeyPrefix.DISTRIBUTE_LOCK + ":" + mqdto.getDate().getTime() + ':' + mqdto.getTrainCode();
        String lockId = SnowUtils.nextSnowIdStr();
        if (Boolean.FALSE.equals(stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockId, 30L, TimeUnit.SECONDS))) {
            log.info("当前线程没有抢到车次：{}的分布式锁，退出争抢", lockKey);
            return;
        }
        log.info("成功拿到锁locKey：{},lockV：{}", lockKey, lockId);
        //添加定时任务实现看门狗机制，自动续命
        Thread demo = new Thread(() -> {
            while (true) {
                Boolean expire = stringRedisTemplate.expire(lockKey, 30, TimeUnit.SECONDS);//有可能已经主动删除key,不需要在续命
                if (Boolean.FALSE.equals(expire)) {
                    log.info("该锁{}已经被删除", lockKey);
                    return;
                }
                log.info("该锁{}被续命了30秒", lockKey);
                try {
                    //每隔十秒就进行检测
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        demo.setDaemon(true);
        demo.start();

        //到这一步说明已经拿到锁了,所以要加异常处理，在finally当中将释放锁
        try {
            LambdaQueryWrapper<ConfirmOrder> confirmOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
            confirmOrderLambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(mqdto.getDate()), ConfirmOrder::getDate, mqdto.getDate());
            confirmOrderLambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(mqdto.getTrainCode()), ConfirmOrder::getTrainCode, mqdto.getTrainCode());
            confirmOrderLambdaQueryWrapper.eq(ConfirmOrder::getStatus, ConfirmOrderStatusTypeEnum.INIT.getCode());//找出所有该车次初始化的订单

            while (true) {
                PageHelper.startPage(1, 20);//每次处理20个订单
                List<ConfirmOrder> confirmOrdersDB = confirmOrderMapper.selectList(confirmOrderLambdaQueryWrapper);
                if (CollUtil.isEmpty(confirmOrdersDB)) {
                    log.info("车次{}已经暂时没有要处理的订单", lockKey);
                    break;
                }
                ConfirmOrder confirmOrder0 = confirmOrdersDB.get(0);
                WebDailyTrainTicketQueryReq deleteCache = new WebDailyTrainTicketQueryReq(); //构建一个用于删除缓存的请求
                deleteCache.setStart(confirmOrder0.getStart());
                deleteCache.setEnd(confirmOrder0.getEnd());
                deleteCache.setStartDate(confirmOrder0.getDate());

                //使用延迟双删，删除缓存
                dailyTrainTicketService.deleteTicketCache(deleteCache);

                log.info("本轮将要处理{}条订单", confirmOrdersDB.size());

                for (ConfirmOrder confirmOrder : confirmOrdersDB) {//这里使用休眠使得排队情况等待的效果变得明显
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //将订单的状态设置为处理中
                    confirmOrder.setStatus(ConfirmOrderStatusTypeEnum.PENDING.getCode());
                    confirmOrderMapper.updateById(confirmOrder);
                    try {
                        sell(confirmOrder);
                    } catch (Exception e) {

                        if (e instanceof BusinessException businessException) {
                            if (businessException.getExceptionEnum().equals(BussinessExceptionEnum.TICKET_INSUFFICIENT_ERROR)) {
                                confirmOrder.setStatus(ConfirmOrderStatusTypeEnum.EMPTY.getCode());
                                log.info("订单{}的余票不足,将处理下一个订单", confirmOrder.getId());
                            } else {
                                log.info("订单{}的购票出现业务异常,将处理下一个订单", confirmOrder.getId());
                                confirmOrder.setStatus(ConfirmOrderStatusTypeEnum.FAILURE.getCode());
                            }
                        } else {
                            throw e;
                        }
                    } finally {
                        confirmOrderMapper.updateById(confirmOrder);//更新订单的状态
                    }
                }

                new Thread(()->{
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        //更新玩一批订单之后延迟一秒之后再删一次
                        dailyTrainTicketService.deleteTicketCache(deleteCache);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } finally {
            //解锁
            if (!Objects.equals(stringRedisTemplate.opsForValue().get(lockKey), lockId)) {
                log.error("当前获取的锁中的ID与当前线程中生成的ID{}不一致", lockId);
                throw new BusinessException(BussinessExceptionEnum.SERVER_BUSY);
            } else {
                log.info("成功释放锁locKey：{},lockV：{}", lockKey, lockId);
                stringRedisTemplate.delete(lockKey);
            }
        }

        //选座
        //挑选符合条件的座位,如果该车厢不满足条件则进入下一个车厢(多个选座应该在同一个车厢)
        //选中座位之后的处理
        //修改座位表的情况
        //余票详情表的余票
        //添加会员购买记录
    }

    private void sell(final ConfirmOrder confirmOrder) {
        // 车次是否存在、余票是否存在、车次是否在有效期之内 ticket条数大于0 同乘客同车次是否已经买过
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketMapper.selectById(confirmOrder.getDailyTrainTicketId());
        if (ObjectUtils.isEmpty(dailyTrainTicket)) {
            throw new BusinessException(BussinessExceptionEnum.NO_TRAIN_INFO);
        }
        log.info("查出余票记录{}", dailyTrainTicket.toString());

        List<ConfirmOrderTicketReq> tickets = JSON.parseArray(confirmOrder.getTickets(), ConfirmOrderTicketReq.class);
        //修改该车次的余票信息，在内存中先扣减票数
        reduceTickets(tickets, dailyTrainTicket);

        Long dailyTrainId = dailyTrainTicket.getDailyTrainId();
        //用于装被选择的座位，这些座位被选择好之后，信息在数据库当中需要被更新
        List<DailyTrainSeat> res = new ArrayList<>();
        //计算ticket选座的偏移值
        ConfirmOrderTicketReq ticketReq0 = tickets.get(0);
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
            //先获取绝对的偏移量,也就是相对于第一排第一个座位的偏移量
            List<Integer> offsetList = tickets.stream().map(confirmOrderTicketReq -> referSeatList.indexOf(confirmOrderTicketReq.getSeat())).toList();
            //再将所有元素减去第一个座位的绝对偏移量
            Integer firstAbsoluteOffset = offsetList.get(0);
            //获取每个座位相对于第一个座位的偏移量
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
        log.info("选座完成，被选择的座位:{}", res);

        if (res.size() < tickets.size()) {
            //座位没有选满
            throw new BusinessException(BussinessExceptionEnum.TICKET_INSUFFICIENT_ERROR);
        }

        if (CollUtil.isNotEmpty(res)) {
            try {
                confirmOrderAfterService.doAfterConfirm(dailyTrainTicket, res, tickets, confirmOrder);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(BussinessExceptionEnum.SERVER_BUSY);
            }
        }
    }


    /**
     * 一个车厢一个车厢的查找,合适的座位
     * @param res 用于承载选座结果的集合
     * @param dailyTrainId 车次的id
     * @param seatTypeCode 表示座位的类型，用于选择车厢
     * @param startIndex 订单开始的车站
     * @param endIndex 订单结束的车站
     * @param seatCol 订单中第一个座位的作为类型
     * @param offsetList 每个座位相对于第一个座位的座位偏移量
     */
    private void getSeat(List<DailyTrainSeat> res, Long dailyTrainId, String seatTypeCode, Integer startIndex, Integer endIndex, String seatCol, List<Integer> offsetList) {
        //该集合用于避免座位的重复选择
        HashSet<Long> set = new HashSet<>();
        res.forEach(seat -> set.add(seat.getId()));
        //一个车厢一个车厢的查找
        LambdaQueryWrapper<DailyTrainCarriage> dailyTrainCarriageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dailyTrainCarriageLambdaQueryWrapper.orderByAsc(DailyTrainCarriage::getTrainIndex);
        dailyTrainCarriageLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainId), DailyTrainCarriage::getDailyTrainId, dailyTrainId);
        List<DailyTrainCarriage> dailyTrainCarriages = dailyTrainCarriageMapper.selectList(dailyTrainCarriageLambdaQueryWrapper);
        log.info("查找到的车厢数目为" + dailyTrainCarriages.size());
        //遍历每一节车厢,查找车座
        for (DailyTrainCarriage carriage : dailyTrainCarriages) {
            List<DailyTrainSeat> tempList = new ArrayList<>();
            if (!carriage.getSeatType().equals(seatTypeCode)) {
                log.info("座位类型与车位类型不匹配,当前车厢座位类型：{},目标的席位类型：{},将继续寻找下一列车厢", carriage.getSeatType(), seatTypeCode);
                continue;
            }
            //找出所有的座位
            Long dailyCarriageId = carriage.getId();
            LambdaQueryWrapper<DailyTrainSeat> dailyTrainSeatLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dailyTrainSeatLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyCarriageId), DailyTrainSeat::getCarriageId, dailyCarriageId);
            dailyTrainSeatLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainId), DailyTrainSeat::getDailyTrainId, dailyTrainId);
            dailyTrainSeatLambdaQueryWrapper.orderByAsc(DailyTrainSeat::getCarriageSeatIndex);//按照座位序号升序排列
            //从数据库当中查找当前车厢得到所有的车座
            List<DailyTrainSeat> seatList = dailyTrainSeatMapper.selectList(dailyTrainSeatLambdaQueryWrapper);
            log.info("查找到的车厢{}的座位数为" + seatList.size(), carriage.getTrainIndex());


            for (int i = 0; i < seatList.size(); i++) {
                DailyTrainSeat dailyTrainSeat = seatList.get(i);

                if (!StringUtils.hasText(seatCol)) {
                    log.info("该订单中，用户没有进行选座，");
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
                if (!set.contains(checkedSeat.getId()) && canSell(checkedSeat, startIndex, endIndex)) {
                    log.info("座位{}符合条件,可以被选中", dailyTrainSeat.getCarriageSeatIndex());
                    tempList.add(checkedSeat);
                } else {
                    log.info("没有选中座位{},将迭代至下一个座位", dailyTrainSeat.getCarriageSeatIndex());
                    continue;
                }

                boolean isGetAllOffsetSeat = true;//该变量用于判断本轮选票中，是否订单中所有的座位都选到了合适的座位
                if (CollUtil.isNotEmpty(offsetList)) {
                    log.info("偏移值有{}可选", offsetList);
                    for (int j = 1; j < offsetList.size(); j++) {
                        int nextIndex = offsetList.get(j) + i;

                        if (nextIndex >= seatList.size()) {
                            log.info("下一个偏移量指向了另一节车厢，查找失败");
                            isGetAllOffsetSeat = false;
                            break;
                        }
                        DailyTrainSeat nextDailyTrainSeat = BeanUtil.copyProperties(seatList.get(nextIndex), DailyTrainSeat.class);
                        if (!set.contains(nextDailyTrainSeat.getId()) && canSell(nextDailyTrainSeat, startIndex, endIndex)) {
                            log.info("座位{}被选中", nextDailyTrainSeat.getCarriageSeatIndex());
                            tempList.add(nextDailyTrainSeat);
                        } else {
                            log.info("没有选中座位{}", nextDailyTrainSeat.getCarriageSeatIndex());
                            isGetAllOffsetSeat = false;
                            break;
                        }
                    }
                }
                if (!isGetAllOffsetSeat) {
                    tempList = new ArrayList<>();
                    continue;
                }
                //完成订单的座位选择，并返回
                res.addAll(tempList);
                return;
            }
        }
    }

    /**
     * 判断当前座位 在指定车站区间内是否能够购买
     * @param dailyTrainSeat
     * @param startIndex
     * @param endIndex
     * @return
     */
    private boolean canSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex) {
        String sell = dailyTrainSeat.getSeatSell();
        String part = sell.substring(startIndex, endIndex);
        if (Integer.parseInt(part) > 0) {
            log.info("该座位已经被购买");
            return false;
        } else {
            char[] charArray = sell.toCharArray();
            Arrays.fill(charArray, startIndex, endIndex, '1');
            dailyTrainSeat.setSeatSell(String.valueOf(charArray));
            log.info("该座位可以被购买，并且sell属性已被更新,更新前{}，更新后{}", sell, dailyTrainSeat.getSeatSell());
            return true;
        }
    }

    /**
     * 在内存中预扣减票
     * @param tickets
     * @param dailyTrainTicket
     */
    private static void reduceTickets(List<ConfirmOrderTicketReq> tickets, DailyTrainTicket dailyTrainTicket) {
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




