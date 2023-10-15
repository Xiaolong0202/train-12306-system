package com.lxl.business.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxl.business.domain.TrainToken;
import com.lxl.business.mapper.TrainTokenMapper;
import com.lxl.business.req.ConfirmOrderDoReq;
import com.lxl.business.req.ConfirmOrderQueryReq;
import com.lxl.common.constant.MQ_TOPIC;
import com.lxl.common.constant.RedisKeyPrefix;
import com.lxl.common.context.MemberInfoContext;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.utils.SnowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/15  19:03
 **/
@Service
@Slf4j
public class ConfirmOrderBeforeService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    TrainTokenMapper trainTokenMapper;

    public static final int tryTimes = 3;//设置重试获取锁的次数

    public void doConfirmBefore(ConfirmOrderDoReq req){
        Date now = new Date(System.currentTimeMillis());
        Long memberId = MemberInfoContext.getMemberId();

        validateToken(memberId, req.getDate(), req.getTrainCode());

//        lock.lock();//加锁
        //使用redis加上分布式锁
        String lockKey = RedisKeyPrefix.DISTRIBUTE_LOCK + ":" +req.getDate().getTime() + ':' + req.getTrainCode() + ':' + req.getDailyTrainTicketId();
        String lockId = SnowUtils.nextSnowIdStr();
        for (int i = 0; true; i++) {
            if (Boolean.TRUE.equals(stringRedisTemplate.opsForValue()
                    .setIfAbsent(lockKey, lockId, 30L, TimeUnit.SECONDS))) {
                break;
            }
            if (i == tryTimes) {
                log.info("经过三次重试，客户{}依然没有抢到锁，请求被驳回！", memberId);
                throw new BusinessException(BussinessExceptionEnum.SERVER_BUSY);
            }
        }
        try {
            log.info("成功拿到锁locKey：{},lockV：{}", lockKey, lockId);
            //添加定时任务实现看门狗机制，自动续命
            Thread demo = new Thread(() -> {
                while (true) {
                    Boolean expire = stringRedisTemplate.expire(lockKey, 30, TimeUnit.SECONDS);//有可能已经主动删除key,不需要在续命
                    if (Boolean.FALSE.equals(expire)) {
                        log.info("该锁{}已经被删除", lockKey);
                        return;
                    }
                    try {
                        //每隔十秒就进行检测
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            demo.setDaemon(true);
            demo.start();
            rocketMQTemplate.convertAndSend(MQ_TOPIC.CONFIRM_ORDER, MessageBuilder.withPayload(req.getTickets().toString()));
            log.info("会员{}订单前的校验完成，将使用MQ去异步化购票",memberId);
        }catch (Exception e){
            //解锁
            if (!Objects.equals(stringRedisTemplate.opsForValue().get(lockKey), lockId)) {
                log.error("当前获取的锁中的ID与当前线程中生成的ID{}不一致", lockId);
                throw new BusinessException(BussinessExceptionEnum.SERVER_BUSY);
            } else {
                log.info("成功释放锁locKey：{},lockV：{}", lockKey, lockId);
                stringRedisTemplate.delete(lockKey);
            }
            throw e;
        }
    }

    private void validateToken(Long memberId, Date date, String trainCode) {
        Boolean setSuccess = stringRedisTemplate.opsForValue().setIfAbsent(RedisKeyPrefix.TRAIN_TOKEN_LOCK + ":" + date.getTime() + ":" + trainCode + ":" + memberId, String.valueOf(memberId), 5, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(setSuccess)) {
            log.info("会员{}在5秒钟之类下了一次单了", memberId);
            throw new BusinessException(BussinessExceptionEnum.FREQUENT_VISITS);
        }

        String redisTokenCountKey = RedisKeyPrefix.TRAIN_TOKEN_COUNT + ":" + date.getTime() + ":" + trainCode;
        String countStr = stringRedisTemplate.opsForValue().getAndExpire(redisTokenCountKey, 60, TimeUnit.SECONDS);
        if (ObjectUtil.isNotEmpty(countStr) && NumberUtil.isInteger(countStr)) {
            //如果令牌存存在
            log.info("缓存中有该车次{}令牌大闸的缓存", redisTokenCountKey);
            Long decrementRes = stringRedisTemplate.opsForValue().decrement(redisTokenCountKey);
            if (decrementRes < 0) {
                //该令牌值已经用尽，将该key删除
                stringRedisTemplate.delete(redisTokenCountKey);
                log.info("令牌{}已经用尽！", redisTokenCountKey);
                throw new BusinessException(BussinessExceptionEnum.TICKET_MAY_SELLOUT);
            }
            log.info("令牌{}的余数：{}", redisTokenCountKey, decrementRes);
            if (decrementRes % 10 == 0) {
                //每隔10个令牌就更新一下数据库
                int update = trainTokenMapper.decreaseToken(date, trainCode, 10);
                if (update <= 0) {
                    throw new BusinessException(BussinessExceptionEnum.TICKET_MAY_SELLOUT);
                }
                log.info("将令牌大闸{}的数据跟新至数据库当中", redisTokenCountKey);
            }
        } else {
            log.info("令牌{}不存在，需要去数据库当中获取", redisTokenCountKey);
            LambdaQueryWrapper<TrainToken> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TrainToken::getStartDate, date);
            queryWrapper.eq(TrainToken::getTrainCode, trainCode);
            TrainToken trainTokenDB = trainTokenMapper.selectOne(queryWrapper);
            if (ObjectUtil.isEmpty(trainTokenDB)) {
                log.info("数据库当中没有对应的令牌信息{}", redisTokenCountKey);
                throw new BusinessException(BussinessExceptionEnum.TICKET_MAY_SELLOUT);
            }
            stringRedisTemplate.opsForValue().set(redisTokenCountKey, String.valueOf(trainTokenDB.getTokenCount() - 1), 60, TimeUnit.SECONDS);
            log.info("将数据库当中的令牌信息{}放入redis当中", redisTokenCountKey);
        }
        log.info("会员{}在购票过程中成功的获取到令牌,可以参与下单", memberId);
    }


}
