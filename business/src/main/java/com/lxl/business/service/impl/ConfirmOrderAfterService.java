package com.lxl.business.service.impl;

import com.lxl.business.domain.DailyTrainSeat;
import com.lxl.business.domain.DailyTrainTicket;
import com.lxl.business.mapper.DailyTrainSeatMapper;
import com.lxl.business.mapper.DailyTrainTicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/6  23:11
 **/
@Service
public class ConfirmOrderAfterService {

    @Autowired
    DailyTrainSeatMapper dailyTrainSeatMapper;

    @Autowired
    DailyTrainTicketMapper dailyTrainTicketMapper;

    @Transactional
    public void updateSellById(DailyTrainTicket dailyTrainTicket, List<DailyTrainSeat> dailyTrainSeats) {
        for (DailyTrainSeat dailyTrainSeat : dailyTrainSeats) {
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
            dailyTrainTicketMapper.updateBySell(dailyTrainTicket.getDailyTrainId(),dailyTrainSeat.getSeatType(),minStart,maxStart,minEnd,maxEnd);
        }

        /**
         *  100001
         *  101101
         *
         */


    }
}
