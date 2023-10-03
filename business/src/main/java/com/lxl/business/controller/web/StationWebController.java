package com.lxl.business.controller.web;

import com.lxl.business.req.StationQueryReq;
import com.lxl.business.req.TrainStationQueryReq;
import com.lxl.business.service.StationService;
import com.lxl.business.service.TrainStationService;
import com.lxl.common.resp.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/3  23:58
 **/
@RequestMapping("/station")
@RestController
public class StationWebController {

    @Autowired
    StationService stationService;

    @GetMapping("/query-list")
    public CommonResp<?> queryList(StationQueryReq req){
        return CommonResp.buildSuccess( stationService.queryList(req),"查询成功");
    }


}
