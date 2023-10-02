package com.lxl.batch.job;

import com.lxl.batch.feign.BusinessFeign;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/2  21:11
 **/
@Slf4j
@Component
public class BusinessHelloJob implements Job {

    @Autowired
    BusinessFeign businessFeign;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String hello = businessFeign.hello();
        log.info("hello = " + hello);
    }
}
