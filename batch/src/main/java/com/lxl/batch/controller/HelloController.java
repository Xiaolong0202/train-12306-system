package com.lxl.batch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/30  10:47
 **/
@RestController
public class HelloController {

    @GetMapping("hello")
    public String Hello(){
        return "hello";
    }

}
