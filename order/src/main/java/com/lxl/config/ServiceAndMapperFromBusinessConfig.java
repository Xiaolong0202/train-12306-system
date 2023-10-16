package com.lxl.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/16  15:20
 **/
@ComponentScan({"com.lxl.business.service","com.lxl.business.mapper","com.lxl.business.feign"})
@Configuration
public class ServiceAndMapperFromBusinessConfig {
}
