package com.lxl.business.controller.web;

import com.lxl.business.req.ConfirmOrderDoReq;
import com.lxl.business.req.ConfirmOrderQueryReq;
import com.lxl.business.service.ConfirmOrderService;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description confirmOrder-12306-system
 * @DateTime 2023/9/25  21:41
 **/
@RequestMapping("/confirmOrder")
@RestController
public class ConfirmOrderWebController {
    @Autowired
    ConfirmOrderService confirmOrderService;

    @GetMapping("/query-list")
    public CommonResp<?> queryList(ConfirmOrderQueryReq req){
        return CommonResp.buildSuccess(confirmOrderService.queryList(req),"查询成功");
    }

    @PostMapping("/do")
    public CommonResp<?> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req){
        confirmOrderService.doConfirm(req);
        return CommonResp.buildSuccess("下单成功!");
    }

}
