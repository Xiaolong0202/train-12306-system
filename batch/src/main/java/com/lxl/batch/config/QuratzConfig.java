package com.lxl.batch.config;

import com.lxl.batch.job.TestJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/30  12:04
 **/
//@Configuration
//public class QuratzConfig {
//    @Bean
//    public JobDetail jobDetail(){
//      return   JobBuilder.newJob(TestJob.class).
//              withIdentity("jobDetail","jobDetail")
//              .usingJobData("key1","hello")
//              .storeDurably()
//              .build();
//    }
//
//    @Bean
//    public Trigger trigger(){
//        return TriggerBuilder.newTrigger()
//                .forJob(jobDetail())
//                .withIdentity("trigger","trigger")
//                .startNow()
//                .withSchedule(CronScheduleBuilder.cronSchedule("*/2 * * * * ?"))
//                .build();
//    }
//}
