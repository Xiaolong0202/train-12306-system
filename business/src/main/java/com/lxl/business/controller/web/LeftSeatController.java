package com.lxl.business.controller.web;

import com.lxl.business.domain.DailyTrainSeat;
import com.lxl.business.resp.DailyTrainSeatQueryResp;
import com.lxl.business.service.DailyTrainSeatService;
import com.lxl.common.resp.CommonResp;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/17  23:49
 **/
@RequestMapping("/leftSeat")
@RestController
public class LeftSeatController {

    @Autowired
    DailyTrainSeatService dailyTrainSeatService;

    @GetMapping("/{dailyTrainId}")
    public CommonResp<?> leftSeatOfOneDailyTrain(@PathVariable("dailyTrainId") Long dailyTrainId){
       List<DailyTrainSeatQueryResp> dailyTrainSeats = dailyTrainSeatService.selectListByDailyTrainId(dailyTrainId);
       return CommonResp.buildSuccess(dailyTrainSeats,"查询对应的车次信息");
    }
}
