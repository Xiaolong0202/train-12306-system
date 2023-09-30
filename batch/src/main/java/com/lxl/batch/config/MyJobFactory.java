package com.lxl.batch.config;

import jakarta.annotation.Resource;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/30  15:47
 **/
 @Configuration
public class MyJobFactory extends SpringBeanJobFactory {

    //AutowireCapableBeanFactory
    @Resource
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    //将父类创建出来的类进行装配
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        autowireCapableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
