package com.lxl.business.controller.admin;

import com.lxl.business.req.TrainCarriageQueryReq;
import com.lxl.business.req.TrainCarriageSaveOrEditReq;
import com.lxl.business.service.TrainCarriageService;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description trainCarriage-12306-system
 * @DateTime 2023/9/25  21:41
 **/
@RequestMapping("/trainCarriage/admin")
@RestController
public class TrainCarriageController {
    @Autowired
    TrainCarriageService trainCarriageService;
    @PostMapping("/save")
    public CommonResp<?> saveTrainCarriage(@RequestBody @Valid TrainCarriageSaveOrEditReq req){
        trainCarriageService.save(req);
        return CommonResp.buildSuccess("保存成功");
    }

    @GetMapping("/query-list")
    public CommonResp<?> queryList(TrainCarriageQueryReq req){
        return CommonResp.buildSuccess(trainCarriageService.queryList(req),"查询成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<?> delete(@PathVariable("id") Long id){
        trainCarriageService.delete(id);
        return CommonResp.buildSuccess("删除成功");
    }

}
