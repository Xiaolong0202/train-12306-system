package com.lxl.member.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/7  23:00
 **/
@RestController
@RefreshScope//该注解可以实时获取值
public class HelloController {

    @Value("${test.nacos}")
    private String v;

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("hello")
    public String hello(){
        return v+"server.port "+serverPort;
    }
}
