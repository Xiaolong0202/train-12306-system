package com.lxl.business.controller.web;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lxl.business.req.ConfirmOrderDoReq;
import com.lxl.business.req.ConfirmOrderQueryReq;
import com.lxl.business.service.ConfirmOrderService;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description confirmOrder-12306-system
 * @DateTime 2023/9/25  21:41
 **/
@Slf4j
@RequestMapping("/confirmOrder")
@RestController
public class ConfirmOrderWebController {
    @Autowired
    ConfirmOrderService confirmOrderService;

    @GetMapping("/query-list")
    public CommonResp<?> queryList(ConfirmOrderQueryReq req){
        return CommonResp.buildSuccess(confirmOrderService.queryList(req),"查询成功");
    }

    @SentinelResource(value = "ConfirmOrderWebController.doConfirm",blockHandler = "doConfirmBlockerHandle")
    @PostMapping("/do")
    public CommonResp<?> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req){
        confirmOrderService.doConfirm(req);
        return CommonResp.buildSuccess("下单成功!");
    }


    public CommonResp<?> doConfirmBlockerHandle(ConfirmOrderDoReq req, BlockException e){
        log.info("当前请求：{}访问量超出限流规则",req);
//        throw new BusinessException(BussinessExceptionEnum.SERVER_BUSY);
        return CommonResp.buildFailure("当前抢票人数过多，请稍后重试");
    }

}
