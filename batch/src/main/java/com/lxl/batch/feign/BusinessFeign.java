package com.lxl.batch.feign;

import com.lxl.batch.feign.fallback.BusinessFeignFallBack;
import com.lxl.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/2  21:09
 **/
@Service
@FeignClient(value = "business",fallback = BusinessFeignFallBack.class)
//@FeignClient(name = "business" , url="http://127.0.0.1:8002/business")
public interface BusinessFeign {

    @RequestMapping("/business/hello")
    public String hello();

    @RequestMapping("/business/dailyTrain/admin/gen-daily/{date}")
    public CommonResp<?> genDaily(
            @PathVariable("date")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);

}
