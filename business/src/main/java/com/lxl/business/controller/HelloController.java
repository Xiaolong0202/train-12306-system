package com.lxl.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/25  18:02
 **/
@RestController
public class HelloController {

    @Autowired
    Environment environment;

    @RequestMapping("hello")
    public String hello() throws InterruptedException {
        Thread.sleep(500);
        return "hello"+environment.getProperty("server.port");
    }
}
