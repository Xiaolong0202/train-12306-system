package com.lxl.batch.job;

import com.lxl.batch.feign.BusinessFeign;
import com.lxl.common.resp.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/2  22:45
 **/
@Slf4j
@Component
public class BusinessGenDailyJob implements Job {

    @Autowired
    BusinessFeign businessFeign;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        /**
         * 生成十五天以后的所有车次
         */
        Date dateAfter15Days = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
        CommonResp<?> commonResp = businessFeign.genDaily(dateAfter15Days);
        log.info("生成每日车次的结果 = " + commonResp);
    }
}
