package com.lxl.business.controller.admin;

import com.lxl.business.req.DailyTrainStationQueryReq;
import com.lxl.business.req.DailyTrainStationSaveOrEditReq;
import com.lxl.business.service.DailyTrainStationService;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description dailyDailyTrainStation-12306-system
 * @DateTime 2023/9/25  21:41
 **/
@RequestMapping("/dailyTrainStation/admin")
@RestController
public class DailyTrainStationController {
    @Autowired
    DailyTrainStationService dailyTrainStationService;
    @PostMapping("/save")
    public CommonResp<?> saveDailyTrainStation(@RequestBody @Valid DailyTrainStationSaveOrEditReq req){
        dailyTrainStationService.save(req);
        return CommonResp.buildSuccess("保存成功");
    }

    @GetMapping("/query-list")
    public CommonResp<?> queryList(DailyTrainStationQueryReq req){
        return CommonResp.buildSuccess(dailyTrainStationService.queryList(req),"查询成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<?> delete(@PathVariable("id") Long id){
        dailyTrainStationService.delete(id);
        return CommonResp.buildSuccess("删除成功");
    }

}
