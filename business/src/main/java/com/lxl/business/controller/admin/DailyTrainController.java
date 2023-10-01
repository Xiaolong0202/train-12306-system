package com.lxl.business.controller.admin;

import com.lxl.business.req.DailyTrainQueryReq;
import com.lxl.business.req.DailyTrainSaveOrEditReq;
import com.lxl.business.service.DailyTrainService;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description dailyDailyTrain-12306-system
 * @DateTime 2023/9/25  21:41
 **/
@RequestMapping("/dailyTrain/admin")
@RestController
public class DailyTrainController {
    @Autowired
    DailyTrainService dailyDailyTrainService;
    @PostMapping("/save")
    public CommonResp<?> saveDailyTrain(@RequestBody @Valid DailyTrainSaveOrEditReq req){
        dailyDailyTrainService.save(req);
        return CommonResp.buildSuccess("保存成功");
    }

    @GetMapping("/query-list")
    public CommonResp<?> queryList(DailyTrainQueryReq req){
        return CommonResp.buildSuccess(dailyDailyTrainService.queryList(req),"查询成功");
    }


    @GetMapping("/query-one/{dailyDailyTrainId}")
    public CommonResp<?> queryOne(@PathVariable("dailyDailyTrainId") Long dailyDailyTrainId){
        return CommonResp.buildSuccess(dailyDailyTrainService.queryOne(dailyDailyTrainId),"查询成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<?> delete(@PathVariable("id") Long id){
        dailyDailyTrainService.delete(id);
        return CommonResp.buildSuccess("删除成功");
    }



}
