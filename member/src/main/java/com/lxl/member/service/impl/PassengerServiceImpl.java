package com.lxl.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.context.MemberInfoContext;
import com.lxl.exception.BusinessException;
import com.lxl.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.member.domain.Passenger;
import com.lxl.member.mapper.PassengerMapper;
import com.lxl.member.req.PassengerQueryReq;
import com.lxl.member.req.PassengerSaveOrEditReq;
import com.lxl.member.resp.PassengerQueryResp;
import com.lxl.member.service.PassengerService;
import com.lxl.resp.PageResp;
import com.lxl.utils.SnowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  15:39
 **/
@Slf4j
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

    @Override
    public PageResp<PassengerQueryResp> queryList(PassengerQueryReq req) {
        LambdaQueryWrapper<Passenger> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!ObjectUtils.isEmpty(req.getMemberId()),Passenger::getMemberId,req.getMemberId());
        wrapper.orderByDesc(Passenger::getId);

        if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
            log.info("当前页码：{}",req.getCurrentPage());
            log.info("当前页面大小：{}",req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
        }
        List<Passenger> passengers = passengerMapper.selectList(wrapper);
        PageInfo<Passenger> passengerPageInfo = new PageInfo<>(passengers);
        int pages = passengerPageInfo.getPages();
        long total = passengerPageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<PassengerQueryResp> list = new ArrayList<>();
        passengers.forEach(passenger -> {
           PassengerQueryResp queryResp = new PassengerQueryResp();
           BeanUtils.copyProperties(passenger,queryResp);
           list.add(queryResp);
        });
        PageResp<PassengerQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return  pageResp;
    }
}
