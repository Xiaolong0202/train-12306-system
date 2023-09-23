package com.lxl.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxl.context.MemberInfoContext;
import com.lxl.exception.BusinessException;
import com.lxl.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.member.domain.Passenger;
import com.lxl.member.mapper.PassengerMapper;
import com.lxl.member.req.PassengerSaveOrEditReq;
import com.lxl.member.service.PassengerService;
import com.lxl.utils.SnowUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  15:39
 **/
@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    PassengerMapper passengerMapper;

    @Override
    public void save(PassengerSaveOrEditReq req) {
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        Date now = DateTime.now();
        passenger.setMemberId(MemberInfoContext.getMemberId());
        passenger.setId(SnowUtils.nextSnowId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);

        //实现unique约束
        LambdaQueryWrapper<Passenger> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Passenger::getMemberId,passenger.getMemberId());
        lambdaQueryWrapper.eq(Passenger::getIdCard,passenger.getIdCard());
        List<Passenger> passengers = passengerMapper.selectList(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(passengers)){
            //不为空表示已经存在,则抛出异常
            throw new BusinessException(BussinessExceptionEnum.PASSENGER_ALREADY_EXIST);
        }
        passengerMapper.insert(passenger);
    }
}
