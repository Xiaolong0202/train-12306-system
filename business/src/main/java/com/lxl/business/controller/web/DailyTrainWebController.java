package com.lxl.business.controller.web;

import com.lxl.business.req.DailyTrainQueryReq;
import com.lxl.business.req.DailyTrainSaveOrEditReq;
import com.lxl.business.service.DailyTrainService;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description dailyDailyTrain-12306-system
 * @DateTime 2023/9/25  21:41
 **/
@RequestMapping("/dailyTrain")
@RestController
public class DailyTrainWebController {
    @Autowired
    DailyTrainService dailyDailyTrainService;

    @GetMapping("/query-list")
    public CommonResp<?> queryList(DailyTrainQueryReq req){
        return CommonResp.buildSuccess(dailyDailyTrainService.queryList(req),"查询成功");
    }

    @GetMapping("/query-one/{dailyTrainId}")
    public CommonResp<?> queryOne(@PathVariable("dailyTrainId") Long dailyDailyTrainId){
        return CommonResp.buildSuccess(dailyDailyTrainService.queryOne(dailyDailyTrainId),"查询成功");
    }


}
