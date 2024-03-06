package com.lxl.business.service.impl;


import com.lxl.business.domain.DailyTrainSeat;
import com.lxl.business.domain.DailyTrainTicket;
import com.lxl.common.enums.ConfirmOrderStatusTypeEnum;
import com.lxl.business.feign.MemberFeign;

import com.lxl.business.mapper.DailyTrainMapper;
import com.lxl.business.mapper.DailyTrainSeatMapper;
import com.lxl.business.mapper.DailyTrainTicketMapper;
import com.lxl.business.req.ConfirmOrderTicketReq;
import com.lxl.common.domain.ConfirmOrder;
import com.lxl.common.mapper.ConfirmOrderMapper;
import com.lxl.common.req.TicketSaveOrEditReq;
import com.lxl.common.resp.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/6  23:11
 **/
@Slf4j
@Service
public class ConfirmOrderAfterService {

    @Autowired
    DailyTrainSeatMapper dailyTrainSeatMapper;

    @Autowired
    DailyTrainTicketMapper dailyTrainTicketMapper;

    @Autowired
    MemberFeign memberFeign;

    @Autowired
    DailyTrainMapper dailyTrainMapper;

    @Autowired
    ConfirmOrderMapper confirmOrderMapper;

//    @GlobalTransactional
//    @Transactional
    public void doAfterConfirm(DailyTrainTicket dailyTrainTicket, List<DailyTrainSeat> dailyTrainSeats, List<ConfirmOrderTicketReq> tickets, final ConfirmOrder confirmOrder) {
        String trainTypeById = dailyTrainMapper.selectTrainTypeById(dailyTrainTicket.getDailyTrainId());
        for (int j = 0; j < dailyTrainSeats.size(); j++) {
            DailyTrainSeat dailyTrainSeat = dailyTrainSeats.get(j);
            //在数据库中更新选座信息
            dailyTrainSeatMapper.updateBatchSell(dailyTrainSeat);

            char[] charArray = dailyTrainSeat.getSeatSell().toCharArray();
            Integer startIndex = dailyTrainTicket.getStartIndex();
            Integer endIndex = dailyTrainTicket.getEndIndex();
            Integer maxStart = endIndex - 1;
            Integer minEnd = startIndex + 1;
            Integer minStart = 0;
            for (int i = startIndex - 1; i > 0; i--) {
                if (charArray[i] == '1') {
                    minStart = i + 1;
                    break;
                }
            }
            Integer maxEnd = charArray.length;
            for (int i = endIndex; i < charArray.length; i++) {
                if (charArray[i] == '1') {
                    maxEnd = i - 1;
                    break;
                }
            }
            //修改车次的对应的座位类型的余票的数量
            dailyTrainTicketMapper.updateBySell(dailyTrainTicket.getDailyTrainId(), dailyTrainSeat.getSeatType(), minStart, maxStart, minEnd, maxEnd);
            log.info("余座{} {} {} 更新成功",dailyTrainSeat.getId(),dailyTrainSeat.getSeatType(),dailyTrainSeat.getSeatCol());

            ConfirmOrderTicketReq confirmOrderTicketReq = tickets.get(j);

            TicketSaveOrEditReq ticketSaveOrEditReq = new TicketSaveOrEditReq();
            ticketSaveOrEditReq.setMemberId(confirmOrder.getMemberId());
            ticketSaveOrEditReq.setPassengerId(confirmOrderTicketReq.getPassengerId());
            ticketSaveOrEditReq.setDailyTrainTicketId(dailyTrainTicket.getId());
            ticketSaveOrEditReq.setPassengerName(confirmOrderTicketReq.getName());
            ticketSaveOrEditReq.setTrainDate(dailyTrainTicket.getStartDate());
            ticketSaveOrEditReq.setTrainCode(trainTypeById+confirmOrder.getTrainCode());
            ticketSaveOrEditReq.setCarriageIndex(dailyTrainSeat.getCarriageIndex());
            ticketSaveOrEditReq.setSeatRow(dailyTrainSeat.getSeatRow());
            ticketSaveOrEditReq.setSeatCol(dailyTrainSeat.getSeatRow());
            ticketSaveOrEditReq.setStartStation(dailyTrainTicket.getStart());
            ticketSaveOrEditReq.setStartTime(dailyTrainTicket.getStartTime());
            ticketSaveOrEditReq.setEndStation(dailyTrainTicket.getEnd());
            ticketSaveOrEditReq.setEndTime(dailyTrainTicket.getEndTime());
            ticketSaveOrEditReq.setSeatType(dailyTrainSeat.getSeatType());

            //通过openFeign远程调用，生成乘客的车票
            CommonResp<?> save = memberFeign.save(ticketSaveOrEditReq);

            //保存订单的信息
            if (save.isSuccess()){
                confirmOrder.setUpdateTime(new Date());
                confirmOrder.setStatus(ConfirmOrderStatusTypeEnum.SUCCESS.getCode());
                confirmOrderMapper.updateById(confirmOrder);
                log.info("乘客{}的购票信息保存成功",ticketSaveOrEditReq.getPassengerName());
            }else {
                confirmOrder.setUpdateTime(new Date());
                confirmOrder.setStatus(ConfirmOrderStatusTypeEnum.FAILURE.getCode());
                confirmOrderMapper.updateById(confirmOrder);
                log.info("乘客{}的购票信息保存失败",ticketSaveOrEditReq.getPassengerName());
            }

        }
    }
}
