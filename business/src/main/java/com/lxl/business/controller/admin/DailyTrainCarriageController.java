package com.lxl.business.controller.admin;

import com.lxl.business.req.DailyTrainCarriageQueryReq;
import com.lxl.business.req.DailyTrainCarriageSaveOrEditReq;
import com.lxl.business.service.DailyTrainCarriageService;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description dailyTrainCarriage-12306-system
 * @DateTime 2023/9/25  21:41
 **/
@RequestMapping("/dailyTrainCarriage/admin")
@RestController
public class DailyTrainCarriageController {
    @Autowired
    DailyTrainCarriageService dailyTrainCarriageService;
    @PostMapping("/save")
    public CommonResp<?> saveDailyTrainCarriage(@RequestBody @Valid DailyTrainCarriageSaveOrEditReq req){
        dailyTrainCarriageService.save(req);
        return CommonResp.buildSuccess("保存成功");
    }

    @GetMapping("/query-list")
    public CommonResp<?> queryList(DailyTrainCarriageQueryReq req){
        return CommonResp.buildSuccess(dailyTrainCarriageService.queryList(req),"查询成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<?> delete(@PathVariable("id") Long id){
        dailyTrainCarriageService.delete(id);
        return CommonResp.buildSuccess("删除成功");
    }

}
