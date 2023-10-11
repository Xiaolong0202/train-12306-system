package com.lxl.business.service.impl;


import com.lxl.business.BusinessMain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/8  17:49
 **/
@SpringBootTest(classes = BusinessMain.class)
class ConfirmOrderAfterServiceTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void test1(){
        System.out.println("stringRedisTemplate.expire(\"asdas\",20, TimeUnit.SECONDS) = "
                + stringRedisTemplate.expire("a", 20, TimeUnit.SECONDS));
    }
}