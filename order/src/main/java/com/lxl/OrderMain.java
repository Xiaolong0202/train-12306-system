package com.lxl;

import com.lxl.config.ServiceAndMapperFromBusinessConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @Author LiuXiaolong
 * @Description Default (Template) Project
 * @DateTime 2023/10/16  15:04
 **/
@Import(ServiceAndMapperFromBusinessConfig.class)
@EnableFeignClients({"com.lxl.business.feign"})
@SpringBootApplication
public class OrderMain {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain.class,args);
    }
}