package com.lxl.business.service.impl;


import com.lxl.business.BusinessMain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/8  17:49
 **/
@SpringBootTest(classes = BusinessMain.class)
class ConfirmOrderAfterServiceTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void test1(){
//        redisTemplate.opsForValue().set("a",456);
        System.out.println("redisTemplate.opsForValue().get(\"a\") = "
                + redisTemplate.opsForValue().get("a"));
    }
}