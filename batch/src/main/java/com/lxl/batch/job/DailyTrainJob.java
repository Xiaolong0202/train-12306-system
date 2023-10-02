package com.lxl.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/2  20:04
 **/
@Slf4j
public class DailyTrainJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      log.info("生成每日车次开始");
      log.info("生成每日车次结束");
    }
}
