package com.lxl.batch.job;

import org.quartz.*;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/30  12:03
 **/
@DisallowConcurrentExecution//禁用并发执行,防止上一个任务还没有开始，这一个任务就已经开始执行了
public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("TestJob");
    }
}
