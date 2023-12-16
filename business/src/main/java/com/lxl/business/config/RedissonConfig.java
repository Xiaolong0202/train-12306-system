package com.lxl.business.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/12/16  21:49
 **/

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6380")
                .setPassword("040202")
                .setConnectionPoolSize(50)
                .setIdleConnectionTimeout(10000)
                .setConnectTimeout(3000)
                .setTimeout(3000)
                .setDatabase(5);
        return Redisson.create(config);
    }

}
