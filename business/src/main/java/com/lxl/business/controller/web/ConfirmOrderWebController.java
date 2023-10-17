package com.lxl.business.controller.web;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.lxl.business.req.ConfirmOrderDoReq;
import com.lxl.business.req.ConfirmOrderQueryReq;
import com.lxl.business.service.ConfirmOrderService;
import com.lxl.business.service.impl.ConfirmOrderBeforeService;
import com.lxl.common.constant.EnvironmentConstant;
import com.lxl.common.constant.RedisKeyPrefix;
import com.lxl.common.context.MemberInfoContext;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.resp.CommonResp;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
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
    @Autowired
    ConfirmOrderBeforeService confirmOrderBeforeService;
    @Value("${spring.profiles.active}")
    private String env;

    @GetMapping("/query-list")
    public CommonResp<?> queryList(ConfirmOrderQueryReq req){
        return CommonResp.buildSuccess(confirmOrderService.queryList(req),"查询成功");
    }

    @SentinelResource(value = "ConfirmOrderWebController.doConfirm",blockHandler = "doConfirmBlockerHandle")
    @PostMapping("/do")
    public CommonResp<?> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req){
        if (StringUtil.equals(env, EnvironmentConstant.DEV)){
            log.info("当前为开发环境，不需要校验验证码");
        }else {
            //校验验证码
            String realCode = stringRedisTemplate.opsForValue().get(RedisKeyPrefix.CAPTCHA_TOKEN + ":" + req.getCaptchaToken());
            String clientCaptchaCode = req.getCaptchaCode();
            if (ObjectUtil.notEqual(realCode, clientCaptchaCode)){
                log.error("会员{}的验证码{}输入错误", MemberInfoContext.getMemberId(), clientCaptchaCode);
                return CommonResp.buildFailure("验证码输入错误");
            }
            log.info("会员{}的验证码{}校验通过，进行下单操作", MemberInfoContext.getMemberId(), clientCaptchaCode);
        }
        //验证码校验通过，进行下单操作,返回订单ID
        Long confirmOrderId = confirmOrderBeforeService.doConfirmBefore(req);
        return CommonResp.buildSuccess(String.valueOf(confirmOrderId),"订单已经创建，正在排队进行选座");
    }

    @GetMapping("/query-order-queue-status/{confirmOrderId}")
    public CommonResp<?> getOrderQueueStatus(@PathVariable("confirmOrderId") Long confirmOrderId){
        Integer result = confirmOrderService.queryOrderQueueStatus(confirmOrderId);
        return CommonResp.buildSuccess(result,"查找到了对应的信息");
    }

    @PutMapping("/cancel/{confirmOrderId}")
    public CommonResp<?> cancelOrderQue(@PathVariable("confirmOrderId") Long confirmOrderId){
        Integer count = confirmOrderService.cancel(confirmOrderId);
        log.info("订单{}的取消数{}",confirmOrderId,count);
        return CommonResp.buildSuccess(count,"返回取消了的订单数");
    }

    /**
     * 限流blockHandler
     * @param req
     * @param e
     * @return
     */
    public CommonResp<?> doConfirmBlockerHandle(ConfirmOrderDoReq req, BlockException e){
        log.info("当前请求：{}访问量超出限流规则",req);
//        throw new BusinessException(BussinessExceptionEnum.SERVER_BUSY);
        return CommonResp.buildFailure("当前抢票人数过多，请稍后重试");
    }

}
