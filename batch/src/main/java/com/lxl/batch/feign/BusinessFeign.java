package com.lxl.batch.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/2  21:09
 **/
@Service
@FeignClient(name = "business" , url="http://127.0.0.1:8002/business")
public interface BusinessFeign {

    @RequestMapping("hello")
    public String hello();
}
