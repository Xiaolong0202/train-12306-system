package com.lxl.business.controller.web;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.lxl.common.constant.RedisKeyPrefix;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/14  21:20
 **/
@RequestMapping("/captcha")
@RestController
@Slf4j
public class CaptchaController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/{codeToken}")
    public void GetCaptchaCode(@PathVariable("codeToken") String codeToken, HttpServletResponse response) throws IOException {
        log.info("收到{}创建验证码的请求",codeToken);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"hhh.gif\"");//通过设置该属性可以指定文件内容

        GifCaptcha gifCaptcha = CaptchaUtil.createGifCaptcha(90, 28);
        gifCaptcha.setGenerator(new RandomGenerator("1234567890abcdefghijklmnopqrstuvwxyz",5));
        gifCaptcha.createCode();
        String code = gifCaptcha.getCode();
        String captchaCodeKey = RedisKeyPrefix.CAPTCHA_TOKEN + ":" + codeToken;
        stringRedisTemplate.opsForValue().set(captchaCodeKey,code,3, TimeUnit.MINUTES);
        log.info("生成验证码:{},codeToken:{},存放至redis中",code,codeToken);//设置默认三分钟的过期时间

        byte[] imageBytes = gifCaptcha.getImageBytes();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(imageBytes);

        outputStream.flush();
        outputStream.close();
    }
}
