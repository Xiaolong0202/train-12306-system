package com.lxl.business.service.impl;

import com.lxl.business.domain.DailyTrainSeat;
import com.lxl.business.mapper.DailyTrainSeatMapper;
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

    @Transactional
    public void updateSellById(List<DailyTrainSeat> dailyTrainSeats){
        for (DailyTrainSeat dailyTrainSeat : dailyTrainSeats) {
            dailyTrainSeatMapper.updateBatchSell(dailyTrainSeat);
        }
    }
}
