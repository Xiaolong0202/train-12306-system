package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.TrainStation;
import com.lxl.business.domain.TrainStation;
import com.lxl.business.mapper.TrainStationMapper;
import com.lxl.business.req.TrainStationQueryReq;
import com.lxl.business.req.TrainStationSaveOrEditReq;
import com.lxl.business.req.TrainStationQueryReq;
import com.lxl.business.req.TrainStationSaveOrEditReq;
import com.lxl.business.resp.TrainStationQueryResp;
import com.lxl.business.resp.TrainStationQueryResp;
import com.lxl.business.service.TrainStationService;
import com.lxl.business.mapper.TrainStationMapper;
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
 * @description 针对表【trainStation(车次)】的数据库操作Service实现
 * @createDate 2023-09-26 15:50:20
 */
@Slf4j
@Service
public class TrainStationServiceImpl implements TrainStationService{
    @Autowired
    TrainStationMapper trainStationMapper;

    @Override
    public void save(TrainStationSaveOrEditReq req) {
        TrainStation trainStation = BeanUtil.copyProperties(req, TrainStation.class);
        Date now = DateTime.now();
        trainStation.setUpdateTime(now);
        trainStation.setStopTime(new Date(trainStation.getOutTime().getTime()-trainStation.getInTime().getTime()));


            //检查unique(code,stationName)
            LambdaQueryWrapper<TrainStation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(trainStation.getStationName()),TrainStation::getStationName,trainStation.getStationName());
            lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(trainStation.getTrainCode()),TrainStation::getTrainCode,trainStation.getTrainCode());
            List<TrainStation> trainStations = trainStationMapper.selectList(lambdaQueryWrapper);
            if (CollUtil.isNotEmpty(trainStations)){
                TrainStation station = trainStations.get(0);
                if ((station.getStationName().equals(trainStation.getStationName())&&station.getTrainCode().equals(station.getTrainCode()))) {
                    if (!station.getId().equals(trainStation.getId())){
                        //不为空表示已经存在,则抛出异常
                        throw new BusinessException(BussinessExceptionEnum.TRAIN_ALREADY_EXIST);
                    }
                }
            }

        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(trainStation.getTrainIndex()),TrainStation::getTrainIndex,trainStation.getTrainIndex());
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(trainStation.getTrainCode()),TrainStation::getTrainCode,trainStation.getTrainCode());
        trainStations = trainStationMapper.selectList(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(trainStations)){
            TrainStation station = trainStations.get(0);
            if ((station.getTrainIndex().equals(trainStation.getTrainIndex())&&station.getTrainCode().equals(station.getTrainCode()))) {
                if (!station.getId().equals(trainStation.getId())){
                    //不为空表示已经存在,则抛出异常
                    throw new BusinessException(BussinessExceptionEnum.TRAIN_ALREADY_EXIST);
                }
            }
        }

        long milliseconds = trainStation.getOutTime().getTime() - trainStation.getInTime().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        String formattedTime = String.format("%02d:%02d:%02d",
                milliseconds / 3600000, (milliseconds % 3600000) / 60000, (milliseconds % 60000) / 1000);
        try {
            trainStation.setStopTime(sdf.parse(formattedTime));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (ObjectUtils.isEmpty(trainStation.getId())){
            log.info("进行新增");
            //id为空则进行新增
            trainStation.setCreateTime(now);
            trainStation.setId(SnowUtils.nextSnowId());

            trainStationMapper.insert(trainStation);
        }else {
            //id不为空则进行update
            log.info("进行update");
            trainStation.setCreateTime(null);
            trainStationMapper.updateById(trainStation);
        }

    }

    @Override
    public PageResp<TrainStationQueryResp> queryList(TrainStationQueryReq req) {
        LambdaQueryWrapper<TrainStation> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TrainStation::getId);

        if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
            log.info("当前页码：{}",req.getCurrentPage());
            log.info("当前页面大小：{}",req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
        }
        List<TrainStation> trainStations = trainStationMapper.selectList(wrapper);
        PageInfo<TrainStation> trainStationPageInfo = new PageInfo<>(trainStations);
        int pages = trainStationPageInfo.getPages();
        long total = trainStationPageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<TrainStationQueryResp> list = new ArrayList<>();
        trainStations.forEach(trainStation -> {
            TrainStationQueryResp queryResp = new TrainStationQueryResp();
            BeanUtils.copyProperties(trainStation,queryResp);
            list.add(queryResp);
        });
        PageResp<TrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return  pageResp;
    }

    @Override
    public void delete(Long id) {
        trainStationMapper.deleteById(id);
    }
}




