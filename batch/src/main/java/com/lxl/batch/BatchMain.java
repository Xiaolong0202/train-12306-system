package com.lxl.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author LiuXiaolong
 * @Description Default (Template) Project
 * @DateTime 2023/9/30  10:40
 **/
@EnableFeignClients(basePackages = {"com.lxl.batch.feign"})
@SpringBootApplication
public class BatchMain {
    public static void main(String[] args) {
        SpringApplication.run(BatchMain.class);
    }
}