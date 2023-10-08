package com.lxl.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author LiuXiaolong
 * @Description Default (Template) Project
 * @DateTime 2023/9/19  19:17
 **/


@SpringBootApplication
@EnableDiscoveryClient
public class GateWayMain {
    public static void main(String[] args) {
        SpringApplication.run(GateWayMain.class);
    }
}