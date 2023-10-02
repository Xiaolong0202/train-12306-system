package com.lxl.business.controller.admin;

import com.lxl.business.req.DailyTrainSeatQueryReq;
import com.lxl.business.req.DailyTrainSeatSaveOrEditReq;
import com.lxl.business.service.DailyTrainSeatService;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description trailyTrainSeat-12306-system
 * @DateTime 2023/9/25  21:41
 **/
@RequestMapping("/dailyTrainSeat/admin")
@RestController
public class DailyTrainSeatController {
    @Autowired
    DailyTrainSeatService dailyTrainSeatService;
    @PostMapping("/save")
    public CommonResp<?> saveDailyTrainSeat(@RequestBody @Valid DailyTrainSeatSaveOrEditReq req){
        dailyTrainSeatService.save(req);
        return CommonResp.buildSuccess("保存成功");
    }

    @GetMapping("/query-list")
    public CommonResp<?> queryList(DailyTrainSeatQueryReq req){
        return CommonResp.buildSuccess(dailyTrainSeatService.queryList(req),"查询成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<?> delete(@PathVariable("id") Long id){
        dailyTrainSeatService.delete(id);
        return CommonResp.buildSuccess("删除成功");
    }

}
