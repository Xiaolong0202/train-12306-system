package com.lxl.business.controller.web;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lxl.business.req.ConfirmOrderDoReq;
import com.lxl.business.req.ConfirmOrderQueryReq;
import com.lxl.business.service.ConfirmOrderService;
import com.lxl.common.constant.RedisKeyPrefix;
import com.lxl.common.context.MemberInfoContext;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/query-list")
    public CommonResp<?> queryList(ConfirmOrderQueryReq req){
        return CommonResp.buildSuccess(confirmOrderService.queryList(req),"查询成功");
    }

    @SentinelResource(value = "ConfirmOrderWebController.doConfirm",blockHandler = "doConfirmBlockerHandle")
    @PostMapping("/do")
    public CommonResp<?> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req){
        //校验验证码
        String realCode = stringRedisTemplate.opsForValue().get(RedisKeyPrefix.CAPTCHA_TOKEN + ":" + req.getCaptchaToken());
        String ClientCaptchaCode = req.getCaptchaCode();
        if (ObjectUtil.notEqual(realCode, ClientCaptchaCode)){
            log.error("会员{}的验证码{}输入错误", MemberInfoContext.getMemberId(), ClientCaptchaCode);
            return CommonResp.buildFailure("验证码输入错误");
        }
        log.info("会员{}的验证码{}校验通过，进行下单操作", MemberInfoContext.getMemberId(), ClientCaptchaCode);
        //验证码校验通过，进行下单操作
        confirmOrderService.doConfirm(req);
        return CommonResp.buildSuccess("下单成功!");
    }


    public CommonResp<?> doConfirmBlockerHandle(ConfirmOrderDoReq req, BlockException e){
        log.info("当前请求：{}访问量超出限流规则",req);
//        throw new BusinessException(BussinessExceptionEnum.SERVER_BUSY);
        return CommonResp.buildFailure("当前抢票人数过多，请稍后重试");
    }

}
