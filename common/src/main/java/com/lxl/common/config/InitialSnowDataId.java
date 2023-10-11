package com.lxl.common.config;

import cn.hutool.core.util.IdUtil;
import com.lxl.common.utils.SnowUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Properties;

/**
 *
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/11  15:55
 **/
@Component
@Slf4j
public class InitialSnowDataId {

    @Autowired
    Environment environment;

    /**
     * 该类用于初始配置SnowUtil中的机器中心ID,与机器ID这两个问题
     */
    @PostConstruct
    public void initialSnowUtil(){
        Long dataCenterId = environment.getProperty("snow.dataCenterId", Long.class);
        Long machineId = environment.getProperty("snow.machineId", Long.class);
        if (!ObjectUtils.isEmpty(dataCenterId)&&!ObjectUtils.isEmpty(machineId)){
            SnowUtils.setDataCenterId(dataCenterId);
            SnowUtils.setMachineId(machineId);
            SnowUtils.updateSnowflake();
            log.info("设置当前服务雪花算法的dataCenterId：{}",dataCenterId);
            log.info("设置当前服务雪花算法的machineId：{}",machineId);
        }else {
            log.warn("未找到对应雪花算法配置，将会使用默认的配置，这样可能存在一定的风险");
        }
    }

}
