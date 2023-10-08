package com.lxl.business.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/25  18:02
 **/
@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello(String name){
        return "hello"+name;
    }
}
