package com.lxl.business.controller.admin;

import com.lxl.business.req.StationQueryReq;
import com.lxl.business.req.StationSaveOrEditReq;
import com.lxl.business.service.StationService;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/25  21:41
 **/
@RequestMapping("/station/admin")
@RestController
public class StationController {
    @Autowired
    StationService stationService;
    @PostMapping("/save")
    public CommonResp<?> saveStation(@RequestBody @Valid StationSaveOrEditReq req){
        stationService.save(req);
        return CommonResp.buildSuccess("保存成功");
    }

    @GetMapping("/query-list")
    public CommonResp<?> queryList(StationQueryReq req){
        return CommonResp.buildSuccess(stationService.queryList(req),"查询成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<?> delete(@PathVariable("id") Long id){
        stationService.delete(id);
        return CommonResp.buildSuccess("删除成功");
    }
    
}
