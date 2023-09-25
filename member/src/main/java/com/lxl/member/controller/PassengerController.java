package com.lxl.member.controller;

import com.lxl.common.context.MemberInfoContext;
import com.lxl.member.req.passenger.PassengerSaveOrEditReq;
import com.lxl.common.resp.CommonResp;
import com.lxl.member.req.passenger.PassengerQueryReq;
import com.lxl.member.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  15:39
 **/
@RequestMapping("/passenger")
@RestController
public class PassengerController {

    @Autowired
    PassengerService passengerService;
    @PostMapping("/save")
    public CommonResp<?> savePassenger(@RequestBody @Valid PassengerSaveOrEditReq req){
        passengerService.save(req);
        return CommonResp.buildSuccess("保存成功");
    }

    @GetMapping("/query-list")
    public CommonResp<?> queryList(PassengerQueryReq req){
        req.setMemberId(MemberInfoContext.getMemberId());
        return CommonResp.buildSuccess(passengerService.queryList(req),"查询成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<?> delete(@PathVariable("id") Long id){
        passengerService.delete(id);
        return CommonResp.buildSuccess("删除成功");
    }


}
