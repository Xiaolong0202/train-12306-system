package com.lxl.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxl.business.dto.ConfirmOrderMQDTO;
import com.lxl.business.service.ConfirmOrderService;
import com.lxl.common.constant.MQ_TOPIC;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/15  22:35
 **/
@Slf4j
@Service
@RocketMQMessageListener(topic = MQ_TOPIC.CONFIRM_ORDER,consumerGroup = "${rocketmq.consumer.group}",messageModel = MessageModel.CLUSTERING)
public class ConfirmOrderConsumer implements  RocketMQListener<String> {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ConfirmOrderService confirmOrderService;

    @Override
    public void onMessage(String message) {
        log.info("message = " + message);
        ConfirmOrderMQDTO mqdto = JSON.parseObject(message, ConfirmOrderMQDTO.class);
        log.info("MQ获取信息"+mqdto);
        confirmOrderService.doConfirm(mqdto);
    }

}
