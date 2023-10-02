package com.lxl.batch.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/2  20:06
 **/
@Component
@Slf4j
@Aspect
public class JobAspect {

    @Pointcut("execution(* com.lxl.batch.job..*.*(..))")
    public void jobAspect(){}

    @Before("jobAspect()")
    public void doBefore(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        log.info("定时任务 ：类名方法 {} {}  开始执行",signature.getDeclaringTypeName(),signature.getName());
    }

    @After("jobAspect()")
    public void doAfter(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        log.info("定时任务 ：类名方法 {} {}  执行完毕",signature.getDeclaringTypeName(),signature.getName());
    }
}
