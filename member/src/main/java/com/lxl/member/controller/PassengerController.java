package com.lxl.member.controller;

import com.lxl.member.req.PassengerSaveOrEditReq;
import com.lxl.member.service.PassengerService;
import com.lxl.resp.CommonResp;
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
    public CommonResp<?>  savePassenger(@RequestBody @Valid PassengerSaveOrEditReq req){
        passengerService.save(req);
        return CommonResp.buildSuccess("保存成功");
    }


}
