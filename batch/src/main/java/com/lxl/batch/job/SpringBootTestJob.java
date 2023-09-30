package com.lxl.batch.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/30  11:25
 **/
@Component
@EnableScheduling
public class SpringBootTestJob {

    @Scheduled(cron = "0/5 * * * * ?")
    public void test(){
        System.out.println("SpringBootTestJob TEST");
    }
}
