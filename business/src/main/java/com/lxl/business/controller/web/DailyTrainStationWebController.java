package com.lxl.business.controller.web;

import com.lxl.business.domain.DailyTrainStation;
import com.lxl.business.service.DailyTrainStationService;
import com.lxl.common.resp.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/17  19:41
 **/

@RequestMapping("/dailyTrainStation")
@RestController
public class DailyTrainStationWebController {

    @Autowired
    DailyTrainStationService dailyTrainStationService;

    @GetMapping("/{dailyTrainId}")
    public CommonResp<?> getDailyTrainStations(@PathVariable("dailyTrainId") Long dailyTrainId) {
        return CommonResp.buildSuccess(dailyTrainStationService.getDailyTrainStationsById(dailyTrainId), "查询指定车次的车站信成功");
    }
}
