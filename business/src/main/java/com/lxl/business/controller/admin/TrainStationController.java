package com.lxl.business.controller.admin;

import com.lxl.business.req.TrainStationQueryReq;
import com.lxl.business.req.TrainStationSaveOrEditReq;
import com.lxl.business.service.TrainStationService;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description trainStation-12306-system
 * @DateTime 2023/9/25  21:41
 **/
@RequestMapping("/trainStation/admin")
@RestController
public class TrainStationController {
    @Autowired
    TrainStationService passengerService;
    @PostMapping("/save")
    public CommonResp<?> saveTrainStation(@RequestBody @Valid TrainStationSaveOrEditReq req){
        passengerService.save(req);
        return CommonResp.buildSuccess("保存成功");
    }

    @GetMapping("/query-list")
    public CommonResp<?> queryList(TrainStationQueryReq req){
        return CommonResp.buildSuccess(passengerService.queryList(req),"查询成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<?> delete(@PathVariable("id") Long id){
        passengerService.delete(id);
        return CommonResp.buildSuccess("删除成功");
    }

}
