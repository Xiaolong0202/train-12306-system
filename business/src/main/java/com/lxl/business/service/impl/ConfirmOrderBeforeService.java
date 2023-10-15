package com.lxl.business.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxl.business.domain.ConfirmOrder;
import com.lxl.business.domain.TrainToken;
import com.lxl.business.enums.ConfirmOrderStatusTypeEnum;
import com.lxl.business.mapper.ConfirmOrderMapper;
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
    @Autowired
    ConfirmOrderMapper confirmOrderMapper;

    public void doConfirmBefore(ConfirmOrderDoReq req){
        Date now = new Date(System.currentTimeMillis());
        Long memberId = MemberInfoContext.getMemberId();

        validateToken(memberId, req.getDate(), req.getTrainCode());

        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowUtils.nextSnowId());
        confirmOrder.setMemberId(memberId);
        confirmOrder.setDate(req.getDate());
        confirmOrder.setTrainCode(req.getTrainCode());
        confirmOrder.setStart(req.getStart());
        confirmOrder.setEnd(req.getEnd());
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        confirmOrder.setTickets(JSON.toJSONString(req.getTickets()));
        confirmOrder.setStatus(ConfirmOrderStatusTypeEnum.INIT.getCode());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrderMapper.insert(confirmOrder);
        req.setConfirmOrderId(confirmOrder.getId());
        req.setMemberId(memberId);

        rocketMQTemplate.convertAndSend(MQ_TOPIC.CONFIRM_ORDER,JSON.toJSONString(req));
        log.info("会员{}订单前的校验完成，将使用MQ去异步化购票",memberId);
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

    public static void main(String[] args) {
        Date date = new Date(1697383469473L);
        System.out.println("date = " + date);
    }
}
