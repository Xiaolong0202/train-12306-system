package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.*;

import com.lxl.business.mapper.*;
import com.lxl.business.req.DailyTrainQueryReq;
import com.lxl.business.req.DailyTrainSaveOrEditReq;
import com.lxl.business.resp.DailyTrainQueryResp;
import com.lxl.business.service.DailyTrainService;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.resp.PageResp;
import com.lxl.common.utils.SnowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author 13430
 * @description 针对表【daily_dailyTrain(每日车次)】的数据库操作Service实现
 * @createDate 2023-10-01 13:49:18
 */

@Slf4j
@Service
public class DailyTrainServiceImpl implements DailyTrainService {

    @Autowired
    DailyTrainMapper dailyTrainMapper;

    @Autowired
    TrainMapper trainMapper;

    @Autowired
    DailyTrainStationMapper dailyTrainStationMapper;

    @Autowired
    TrainStationMapper trainStationMapper;

    @Autowired
    DailyTrainCarriageMapper dailyTrainCarriageMapper;

    @Autowired
    TrainCarriageMapper trainCarriageMapper;

    @Autowired
    DailyTrainSeatMapper dailyTrainSeatMapper;

    @Autowired
    TrainSeatMapper trainSeatMapper;


    @Override
    public void save(DailyTrainSaveOrEditReq req) {
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);
        Date now = DateTime.now();
        dailyTrain.setUpdateTime(now);


        //如果name被更改了,实现unique约束
        LambdaQueryWrapper<DailyTrain> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrain.getCode()), DailyTrain::getCode, dailyTrain.getCode());
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrain.getStartDate()), DailyTrain::getStartDate, dailyTrain.getStartDate());
        List<DailyTrain> dailyTrains = dailyTrainMapper.selectList(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(dailyTrains)) {
            if (!ObjectUtils.nullSafeEquals(dailyTrains.get(0).getId(), dailyTrain.getId())) {
                //不为空表示已经存在,则抛出异常
                throw new BusinessException(BussinessExceptionEnum.TRAIN_ALREADY_EXIST);
            }
        }

        if (ObjectUtils.isEmpty(dailyTrain.getId())) {
            log.info("进行新增");
            //id为空则进行新增
            dailyTrain.setCreateTime(now);
            dailyTrain.setId(SnowUtils.nextSnowId());

            dailyTrainMapper.insert(dailyTrain);
        } else {
            //id不为空则进行update
            log.info("进行update");
            dailyTrain.setCreateTime(null);
            dailyTrainMapper.updateById(dailyTrain);
        }

    }

    @Override
    public PageResp<DailyTrainQueryResp> queryList(DailyTrainQueryReq req) {
        LambdaQueryWrapper<DailyTrain> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(DailyTrain::getStartDate).orderByDesc(DailyTrain::getId);

        wrapper.eq(!ObjectUtils.isEmpty(req.getStartDate()), DailyTrain::getStartDate, req.getStartDate());

        wrapper.eq(!ObjectUtils.isEmpty(req.getCode()), DailyTrain::getCode, req.getCode());

        if (!ObjectUtils.isEmpty(req.getPageSize()) && !ObjectUtils.isEmpty(req.getCurrentPage())) {
            log.info("当前页码：{}", req.getCurrentPage());
            log.info("当前页面大小：{}", req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(), req.getPageSize());
        }
        List<DailyTrain> dailyTrains = dailyTrainMapper.selectList(wrapper);
        PageInfo<DailyTrain> dailyTrainPageInfo = new PageInfo<>(dailyTrains);
        int pages = dailyTrainPageInfo.getPages();
        long total = dailyTrainPageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<DailyTrainQueryResp> list = new ArrayList<>();
        dailyTrains.forEach(dailyTrain -> {
            DailyTrainQueryResp queryResp = new DailyTrainQueryResp();
            BeanUtils.copyProperties(dailyTrain, queryResp);
            list.add(queryResp);
        });
        PageResp<DailyTrainQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return pageResp;
    }

    @Override
    public void delete(Long id) {
        dailyTrainMapper.deleteById(id);
    }

    @Override
    public DailyTrainQueryResp queryOne(Long dailyTrainId) {
        LambdaQueryWrapper<DailyTrain> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainId), DailyTrain::getId, dailyTrainId);
        DailyTrain dailyTrain = dailyTrainMapper.selectOne(lambdaQueryWrapper);
        return BeanUtil.copyProperties(dailyTrain, DailyTrainQueryResp.class);
    }


    /**
     * @param date 目标日期
     */
    @Transactional
    @Override
    public void genDaily(Date date) {
        //先删除目标日期所有的车次,以及该车次的详情信息
        //先找出目标日期的车次的 id
        List<Long> dailyTrainIds = dailyTrainMapper.selectAimDateTrainIds(date);
        dailyTrainMapper.deleteBatchIds(dailyTrainIds);
        //删除目标日期存在的车站
        dailyTrainIds.forEach(dailyTrainId->{
            Map<String, Object> columnMap = Map.of("daily_train_id", dailyTrainId);
            dailyTrainSeatMapper.deleteByMap(columnMap);
            dailyTrainCarriageMapper.deleteByMap(columnMap);
            dailyTrainStationMapper.deleteByMap(columnMap);
        });


        Date now = new Date(System.currentTimeMillis());
        List<Train> trains = trainMapper.selectList(null);

//        List<DailyTrainStation> dailyTrainStations = new ArrayList<>();
//        List<DailyTrainCarriage> dailyTrainCarriages = new ArrayList<>();
//        List<DailyTrainSeat> dailyTrainSeatList = new ArrayList<>();

        //将所有的train转换为批量的dailyTrains
        List<DailyTrain> dailyTrains = trains.stream().map(train -> {
            DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
            dailyTrain.setId(SnowUtils.nextSnowId());
            dailyTrain.setStartDate(date);
            dailyTrain.setUpdateTime(now);
            dailyTrain.setCreateTime(now);

            //将所有的trainStation转换为批量的dailyTrainStaion
            LambdaQueryWrapper<TrainStation> trainStationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            trainStationLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(train.getId()), TrainStation::getTrainId, train.getId());
            List<TrainStation> trainStations = trainStationMapper.selectList(trainStationLambdaQueryWrapper);

            List<DailyTrainStation> tempDailyTrainStations = trainStations.stream().map(trainStation -> {
                DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
                dailyTrainStation.setId(SnowUtils.nextSnowId());
                dailyTrainStation.setDailyTrainId(dailyTrain.getId());
                dailyTrainStation.setUpdateTime(now);
                dailyTrainStation.setUpdateTime(now);

                return dailyTrainStation;
            }).toList();
            if (CollUtil.isNotEmpty(tempDailyTrainStations)){
                dailyTrainStationMapper.insertBatch(tempDailyTrainStations);
            }
//            dailyTrainStations.addAll(tempDailyTrainStations);

            //生成dailyCarriage
            LambdaQueryWrapper<TrainCarriage> trainCarriageLambdaQueryWrapper = new LambdaQueryWrapper<>();
            trainCarriageLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(train.getId()), TrainCarriage::getTrainId, train.getId());
            List<TrainCarriage> carriages = trainCarriageMapper.selectList(trainCarriageLambdaQueryWrapper);

            List<DailyTrainCarriage> tempDailyTrainCarriages = carriages.stream().map(trainCarriage -> {
                DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(trainCarriage, DailyTrainCarriage.class);
                dailyTrainCarriage.setId(SnowUtils.nextSnowId());
                dailyTrainCarriage.setDailyTrainId(dailyTrain.getId());
                dailyTrainCarriage.setCreateTime(now);
                dailyTrainCarriage.setUpdateTime(now);

                return dailyTrainCarriage;
            }).toList();
            if (CollUtil.isNotEmpty(tempDailyTrainCarriages)){
                dailyTrainCarriageMapper.insertBatch(tempDailyTrainCarriages);
            }
//            dailyTrainCarriages.addAll(tempDailyTrainCarriages);

            //生成dailySeat
            LambdaQueryWrapper<TrainSeat> trainSaetLambdaQueryWrapper = new LambdaQueryWrapper<>();
            trainSaetLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(train.getId()), TrainSeat::getTrainId, train.getId());
            List<TrainSeat> trainSeats = trainSeatMapper.selectList(trainSaetLambdaQueryWrapper);

            //构建sell,字符串的长度应该为该列车次经过的站数的长度-1
            char[] sellChars = new char[Math.max(0,tempDailyTrainStations.size()-1)];
            Arrays.fill(sellChars,'0');
            String sell = String.valueOf(sellChars);

            List<DailyTrainSeat> tempDailyTrainSeatList = trainSeats.stream().map(trainSeat -> {
                DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(trainSeat, DailyTrainSeat.class);
                dailyTrainSeat.setId(SnowUtils.nextSnowId());
                dailyTrainSeat.setDailyTrainId(dailyTrain.getId());
                dailyTrainSeat.setCreateTime(now);
                dailyTrainSeat.setUpdateTime(now);
                dailyTrainSeat.setSell(sell);

                return dailyTrainSeat;
            }).toList();

            if (CollUtil.isNotEmpty(tempDailyTrainSeatList)){
                dailyTrainSeatMapper.insertBatch(tempDailyTrainSeatList);
            }
//            dailyTrainSeatList.addAll(tempDailyTrainSeatList);

            return dailyTrain;
        }).toList();

        //批量生成 每日车站
        dailyTrainMapper.insertBatch(dailyTrains);
//        dailyTrainStationMapper.insertBatch(dailyTrainStations);
//        dailyTrainCarriageMapper.insertBatch(dailyTrainCarriages);
//        dailyTrainSeatMapper.insertBatch(dailyTrainSeatList);
    }


}




