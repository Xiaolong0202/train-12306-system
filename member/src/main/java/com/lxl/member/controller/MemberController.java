package com.lxl.member.controller;

import com.lxl.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("hello")
    public String  hello(){
        return "hello";
    }

    @GetMapping("/count")
    public int conut(){
        return memberService.count();
    }
}
