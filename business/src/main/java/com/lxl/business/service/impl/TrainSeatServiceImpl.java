package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.TrainSeat;
import com.lxl.business.domain.TrainStation;
import com.lxl.business.mapper.TrainSeatMapper;
import com.lxl.business.req.TrainSeatQueryReq;
import com.lxl.business.req.TrainSeatSaveOrEditReq;
import com.lxl.business.resp.TrainSeatQueryResp;
import com.lxl.business.service.TrainSeatService;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.resp.PageResp;
import com.lxl.common.utils.SnowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 13430
 * @description 针对表【trainSeat(车次)】的数据库操作Service实现
 * @createDate 2023-09-26 15:50:20
 */
@Slf4j
@Service
public class TrainSeatServiceImpl implements TrainSeatService{
    @Autowired
    TrainSeatMapper trainSeatMapper;

    @Override
    public void save(TrainSeatSaveOrEditReq req) {
        TrainSeat trainSeat = BeanUtil.copyProperties(req, TrainSeat.class);
        Date now = DateTime.now();
        trainSeat.setUpdateTime(now);


        if (ObjectUtils.isEmpty(trainSeat.getId())){
            log.info("进行新增");
            //id为空则进行新增
            trainSeat.setCreateTime(now);
            trainSeat.setId(SnowUtils.nextSnowId());

            trainSeatMapper.insert(trainSeat);
        }else {
            //id不为空则进行update
            log.info("进行update");
            trainSeat.setCreateTime(null);
            trainSeatMapper.updateById(trainSeat);
        }

    }

    @Override
    public PageResp<TrainSeatQueryResp> queryList(TrainSeatQueryReq req) {
        LambdaQueryWrapper<TrainSeat> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TrainSeat::getId);
        wrapper.eq(!ObjectUtils.isEmpty(req.getTrainId()), TrainSeat::getTrainId,req.getTrainId());

        if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
            log.info("当前页码：{}",req.getCurrentPage());
            log.info("当前页面大小：{}",req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
        }
        List<TrainSeat> trainSeats = trainSeatMapper.selectList(wrapper);
        PageInfo<TrainSeat> trainSeatPageInfo = new PageInfo<>(trainSeats);
        int pages = trainSeatPageInfo.getPages();
        long total = trainSeatPageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<TrainSeatQueryResp> list = new ArrayList<>();
        trainSeats.forEach(trainSeat -> {
            TrainSeatQueryResp queryResp = new TrainSeatQueryResp();
            BeanUtils.copyProperties(trainSeat,queryResp);
            list.add(queryResp);
        });
        PageResp<TrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return  pageResp;
    }

    @Override
    public void delete(Long id) {
        trainSeatMapper.deleteById(id);
    }
}




