package com.lxl.batch.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/30  15:58
 **/
@Configuration
public class SchedulerConfig {

    @Autowired
    private MyJobFactory myJobFactory;

   @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource")DataSource dataSource){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJobFactory(myJobFactory);
        factoryBean.setStartupDelay(2);
        return factoryBean;
   }
}
