package com.lxl.business.service.impl;

import com.lxl.business.domain.DailyTrainSeat;
import com.lxl.business.domain.DailyTrainTicket;
import com.lxl.business.feign.MemberFeign;
import com.lxl.business.mapper.DailyTrainMapper;
import com.lxl.business.mapper.DailyTrainSeatMapper;
import com.lxl.business.mapper.DailyTrainTicketMapper;
import com.lxl.business.req.ConfirmOrderTicketReq;
import com.lxl.common.context.MemberInfoContext;
import com.lxl.common.req.TicketSaveOrEditReq;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void doAfterConfirm(DailyTrainTicket dailyTrainTicket, List<DailyTrainSeat> dailyTrainSeats, List<ConfirmOrderTicketReq> tickets, @NotBlank String trainCode) {
        String trainTypeById = dailyTrainMapper.selectTrainTypeById(dailyTrainTicket.getDailyTrainId());
        for (int j = 0; j < dailyTrainSeats.size(); j++) {
            DailyTrainSeat dailyTrainSeat = dailyTrainSeats.get(j);
            dailyTrainSeatMapper.updateBatchSell(dailyTrainSeat);

            char[] charArray = dailyTrainSeat.getSell().toCharArray();
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
            dailyTrainTicketMapper.updateBySell(dailyTrainTicket.getDailyTrainId(), dailyTrainSeat.getSeatType(), minStart, maxStart, minEnd, maxEnd);
            log.info("余座{} {} {} 更新成功",dailyTrainSeat.getId(),dailyTrainSeat.getSeatType(),dailyTrainSeat.getSeatCol());

            ConfirmOrderTicketReq confirmOrderTicketReq = tickets.get(j);

            TicketSaveOrEditReq ticketSaveOrEditReq = new TicketSaveOrEditReq();
            ticketSaveOrEditReq.setMemberId(MemberInfoContext.getMemberId());
            ticketSaveOrEditReq.setPassengerId(confirmOrderTicketReq.getPassengerId());
            ticketSaveOrEditReq.setDailyTrainTicketId(dailyTrainTicket.getId());
            ticketSaveOrEditReq.setPassengerName(confirmOrderTicketReq.getName());
            ticketSaveOrEditReq.setDate(dailyTrainTicket.getStartDate());
            ticketSaveOrEditReq.setTrainCode(trainTypeById+trainCode);
            ticketSaveOrEditReq.setCarriageIndex(dailyTrainSeat.getCarriageIndex());
            ticketSaveOrEditReq.setRow(dailyTrainSeat.getSeatRow());
            ticketSaveOrEditReq.setCol(dailyTrainSeat.getSeatRow());
            ticketSaveOrEditReq.setStart(dailyTrainTicket.getStart());
            ticketSaveOrEditReq.setStartTime(dailyTrainTicket.getStartTime());
            ticketSaveOrEditReq.setEnd(dailyTrainTicket.getEnd());
            ticketSaveOrEditReq.setEndTime(dailyTrainTicket.getEndTime());
            ticketSaveOrEditReq.setSeatType(dailyTrainSeat.getSeatType());

            CommonResp<?> save = memberFeign.save(ticketSaveOrEditReq);
            if (save.isSuccess()){
                log.info("乘客{}的购票信息保存成功",ticketSaveOrEditReq.getPassengerName());
            }else {
                log.info("乘客{}的购票信息保存失败",ticketSaveOrEditReq.getPassengerName());
            }
        }
    }
}
