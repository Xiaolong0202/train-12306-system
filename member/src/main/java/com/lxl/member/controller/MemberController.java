package com.lxl.member.controller;

import com.lxl.common.resp.CommonResp;
import com.lxl.member.req.member.MemberLoginReq;
import com.lxl.member.req.member.MemberRegisterReq;
import com.lxl.member.req.member.MemberSendCodeReq;
import com.lxl.common.resp.MemberLoginResp;
import com.lxl.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  20:44
 **/
@Slf4j
@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResp<?> conut(){
        return new CommonResp<>(memberService.count());
    }

    @PostMapping("/register")
    public CommonResp<?> register(@RequestBody @Valid MemberRegisterReq req){
        return new CommonResp<>(memberService.register(req.getMobile()));
    }

    @PostMapping("/send-code")
    public CommonResp<?> sendCode(@RequestBody @Valid MemberSendCodeReq req){
        memberService.sendCode(req.getMobile());
        return CommonResp.buildSuccess("验证码发送成功");
    }

    @PostMapping("/login")
    public CommonResp<?> login(@RequestBody @Valid MemberLoginReq req){
        MemberLoginResp resp = memberService.login(req);
        return CommonResp.buildSuccess(resp,"登录成功");
    }

}
