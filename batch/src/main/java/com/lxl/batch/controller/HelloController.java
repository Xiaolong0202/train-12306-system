package com.lxl.batch.controller;

import com.lxl.batch.feign.BusinessFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/30  10:47
 **/
@RestController
public class HelloController {

    @Autowired
    BusinessFeign businessFeign;

    @GetMapping("hello")
    public String Hello(){
        return businessFeign.hello();
    }

}
