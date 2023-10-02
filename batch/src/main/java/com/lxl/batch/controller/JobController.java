package com.lxl.batch.controller;

import com.lxl.batch.req.CronJobReq;
import com.lxl.batch.resp.CronJobResp;
import com.lxl.common.resp.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/30  16:21
 **/
@Slf4j
@RestController
@RequestMapping("/admin/job")
public class JobController {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    @RequestMapping("/run")
    public CommonResp<?> run(@RequestBody CronJobReq cronJobReq){
        String name = cronJobReq.getName();
        String group = cronJobReq.getGroup();
        log.info("开始执行手动任务");
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.triggerJob(JobKey.jobKey(name,group));
        } catch (SchedulerException e) {
            log.error("执行手动任务失败");
            return CommonResp.buildFailure(String.format("执行手动任务%s,失败",name));
        }
        return CommonResp.buildSuccess(String.format("执行手动任务%s,失败",name));
    }

    @RequestMapping("/add")
    public CommonResp<?> add(@RequestBody CronJobReq cronJobReq) {
        String name = cronJobReq.getName();
        String group = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        log.info("添加任务开始");
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.start();
            JobDetail jobDetail = JobBuilder
                    .newJob((Class<? extends Job>) Class.forName(name))
                    .withIdentity(name, group)
                    .build();
            CronTrigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(name, group)
                    .withDescription(description).
                    withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();

            scheduler.scheduleJob(jobDetail,cronTrigger);
        } catch (SchedulerException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
            log.error("创建定时任务的时候失败"+e);
            String errorInfo = String.format("创建定时任务%s,失败",name);
            if (e instanceof ClassNotFoundException exception){
                errorInfo = "请给定一个正确的权限定类名！";
            }
            return CommonResp.buildFailure(errorInfo);
        }
        return CommonResp.buildSuccess(String.format("创建定时任务%s,成功",name));
    }

    @RequestMapping("/pause")
    public CommonResp<?> pause(@RequestBody CronJobReq cronJobReq){
        String name = cronJobReq.getName();
        String group = cronJobReq.getGroup();
        log.info("暂停任务开始");

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.pauseJob(JobKey.jobKey(name,group));
        } catch (SchedulerException e) {
            log.error("暂停定时任务的时候失败"+e);
            return CommonResp.buildFailure(String.format("暂停定时任务%s,失败",name));
        }
        return CommonResp.buildSuccess(String.format("暂停定时任务%s,成功",name));
    }

    /**
     * 重启
     * @param cronJobReq
     * @return
     */
    @RequestMapping("/resume")
    public CommonResp<?> resume(@RequestBody CronJobReq cronJobReq){
        String name = cronJobReq.getName();
        String group = cronJobReq.getGroup();
        log.info("重启定时任务开始");

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.resumeJob(JobKey.jobKey(name,group));
        } catch (SchedulerException e) {
            log.error("重启定时任务的时候失败"+e);
            return CommonResp.buildFailure(String.format("重启定时任务%s,失败",name));
        }
        return CommonResp.buildSuccess(String.format("重启定时任务%s,成功",name));
    }

    @RequestMapping("/reschedule")
    public CommonResp reschedule(@RequestBody CronJobReq cronJobReq){
        String name = cronJobReq.getName();
        String group = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        log.info("更新定时任务开始");

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
            CronTriggerImpl trigger1 = (CronTriggerImpl)scheduler.getTrigger(triggerKey);
            trigger1.setStartTime(new Date());
            CronTrigger trigger = trigger1;
            trigger = trigger.getTriggerBuilder().withIdentity(name, group).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).withDescription(description).build();
            scheduler.rescheduleJob(triggerKey,trigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务的时候失败"+e);
            return CommonResp.buildFailure(String.format("更新定时任务%s,失败",name));
        }
        return CommonResp.buildSuccess(String.format("更新定时任务%s,成功",name));
    }

    @RequestMapping(value = "/delete")
    public CommonResp delete(@RequestBody CronJobReq cronJobReq) {
              String jobClassName = cronJobReq.getName();
              String jobGroupName = cronJobReq.getGroup();
              log.info("删除定时任务开始：{}，{}", jobClassName, jobGroupName);
              CommonResp commonResp = new CommonResp();
              try {
                    Scheduler scheduler = schedulerFactoryBean.getScheduler();
                    scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
                    scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
                    scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
                } catch (SchedulerException e) {
                    log.error("删除定时任务失败:" + e);
                    commonResp.setSuccess(false);
                    commonResp.setMessage("删除定时任务失败:调度异常");
                }
              log.info("删除定时任务结束：{}", commonResp);
              commonResp.setSuccess(true);
              return commonResp;
          }
    @RequestMapping(value="/query")
   public CommonResp query() {
           log.info("查看所有定时任务开始");
           CommonResp commonResp = new CommonResp();
           List<CronJobResp> cronJobDtoList = new ArrayList();
           try {
                   Scheduler scheduler = schedulerFactoryBean.getScheduler();
                   for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                 CronJobResp cronJobResp = new CronJobResp();
                 cronJobResp.setName(jobKey.getName());
                 cronJobResp.setGroup(jobKey.getGroup());

                 List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                 CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
                 cronJobResp.setNextFireTime(cronTrigger.getNextFireTime());
                 cronJobResp.setPreFireTime(cronTrigger.getPreviousFireTime());
                 cronJobResp.setCronExpression(cronTrigger.getCronExpression());
                 cronJobResp.setDescription(cronTrigger.getDescription());
                 Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                 cronJobResp.setState(triggerState.name());
       
                 cronJobDtoList.add(cronJobResp);
             }
       
             }
               } catch (SchedulerException e) {
                   log.error("查看定时任务失败:" + e);
                   commonResp.setSuccess(false);
                   commonResp.setMessage("查看定时任务失败:调度异常");
               }
           commonResp.setContent(cronJobDtoList);
           log.info("查看定时任务结束：{}", commonResp);
           commonResp.setSuccess(true);
           return commonResp;
       }
}
