package com.lxl.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author LiuXiaolong
 * @Description Default (Template) Project
 * @DateTime 2023/9/25  17:22
 **/
@SpringBootApplication
@ComponentScan("com.lxl.*")
public class BusinessMain {
    public static void main(String[] args) {
        SpringApplication.run(BusinessMain.class);
    }
}