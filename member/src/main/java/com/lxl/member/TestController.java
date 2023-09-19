package com.lxl.member;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  17:18
 **/
@RestController
@ComponentScan("com.lxl.*")
public class TestController {

    @GetMapping("hello")
    public String  hello(){
        return "hello";
    }
}
