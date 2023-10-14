package com.lxl.business.controller.admin;

import com.lxl.business.req.TrainSeatQueryReq;
import com.lxl.business.req.TrainTokenQueryReq;
import com.lxl.business.req.TrainTokenUpdateTokenCountReq;
import com.lxl.business.service.TrainTokenService;
import com.lxl.common.resp.CommonResp;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/13  22:46
 **/
@RequestMapping("/trainToken/admin")
@RestController
public class TrainTokenController {

    @Autowired
    TrainTokenService trainTokenService;

    @GetMapping("/query-list")
    public CommonResp<?> queryList(TrainTokenQueryReq req){
        return CommonResp.buildSuccess(trainTokenService.queryList(req),"查询成功");
    }

    @PutMapping("/updateTokenCount")
    public CommonResp<?> updateTokenCount(@RequestBody TrainTokenUpdateTokenCountReq req){
        trainTokenService.updateTokenCount(req.getId(),req.getTokenCount());
        return CommonResp.buildSuccess("修改令牌余量成功");
    }

}
