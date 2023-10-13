package com.lxl.batch.feign.fallback;

import com.lxl.batch.feign.BusinessFeign;
import com.lxl.common.resp.CommonResp;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/13  17:01
 **/
@Component
public class BusinessFeignFallBack implements BusinessFeign {
    @Override
    public String hello() {
        return "Fallback";
    }

    @Override
    public CommonResp<?> genDaily(Date date) {
        return null;
    }
}
