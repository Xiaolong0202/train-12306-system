package com.lxl.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Author LiuXiaolong
 * @Description Default (Template) Project
 * @DateTime 2023/9/19  16:53
 **/
@Slf4j
@SpringBootApplication
public class MemberMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MemberMain.class);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info("项目已经启动，地址为   http://127.0.0.1:{}",environment.getProperty("server.port","8080"));
    }
}