package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.DailyTrainStation;
import com.lxl.business.mapper.DailyTrainStationMapper;
import com.lxl.business.req.DailyTrainStationQueryReq;
import com.lxl.business.req.DailyTrainStationSaveOrEditReq;
import com.lxl.business.resp.DailyTrainStationQueryResp;
import com.lxl.business.service.DailyTrainStationService;
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
 * @description 针对表【dailyTrainStation(车次)】的数据库操作Service实现
 * @createDate 2023-09-26 15:50:20
 */
@Slf4j
@Service
public class DailyTrainStationServiceImpl implements DailyTrainStationService{
    @Autowired
    DailyTrainStationMapper dailyTrainStationMapper;

    @Override
    public void save(DailyTrainStationSaveOrEditReq req) {
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(req, DailyTrainStation.class);
        Date now = DateTime.now();
        dailyTrainStation.setUpdateTime(now);


            //检查unique(getDailyTrainId,stationName)
            LambdaQueryWrapper<DailyTrainStation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainStation.getStationName()),DailyTrainStation::getStationName,dailyTrainStation.getStationName());
            lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainStation.getDailyTrainId()),DailyTrainStation::getDailyTrainId,dailyTrainStation.getDailyTrainId());
            List<DailyTrainStation> dailyTrainStations = dailyTrainStationMapper.selectList(lambdaQueryWrapper);
            if (CollUtil.isNotEmpty(dailyTrainStations)){
                DailyTrainStation station = dailyTrainStations.get(0);
                if ((station.getStationName().equals(dailyTrainStation.getStationName())&&station.getDailyTrainId().equals(station.getDailyTrainId()))) {
                    if (!station.getId().equals(dailyTrainStation.getId())){
                        //不为空表示已经存在,则抛出异常
                        throw new BusinessException(BussinessExceptionEnum.TRAIN_STATION_STATION_NAME_ALREADY_EXIST);
                    }
                }
            }

        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainStation.getTrainIndex()),DailyTrainStation::getTrainIndex,dailyTrainStation.getTrainIndex());
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainStation.getDailyTrainId()),DailyTrainStation::getDailyTrainId,dailyTrainStation.getDailyTrainId());
        dailyTrainStations = dailyTrainStationMapper.selectList(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(dailyTrainStations)){
            DailyTrainStation station = dailyTrainStations.get(0);
            if ((station.getTrainIndex().equals(dailyTrainStation.getTrainIndex())&&station.getDailyTrainId().equals(station.getDailyTrainId()))) {
                if (!station.getId().equals(dailyTrainStation.getId())){
                    //不为空表示已经存在,则抛出异常
                    throw new BusinessException(BussinessExceptionEnum.TRAIN_STATION_INDEX_ALREADY_EXIST);
                }
            }
        }

        long milliseconds = dailyTrainStation.getOutTime().getTime() - dailyTrainStation.getInTime().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        String formattedTime = String.format("%02d:%02d:%02d",
                milliseconds / 3600000, (milliseconds % 3600000) / 60000, (milliseconds % 60000) / 1000);
        try {
            dailyTrainStation.setStopTime(sdf.parse(formattedTime));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (ObjectUtils.isEmpty(dailyTrainStation.getId())){
            log.info("进行新增");
            //id为空则进行新增
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setId(SnowUtils.nextSnowId());

            dailyTrainStationMapper.insert(dailyTrainStation);
        }else {
            //id不为空则进行update
            log.info("进行update");
            dailyTrainStation.setCreateTime(null);
            dailyTrainStationMapper.updateById(dailyTrainStation);
        }

    }

    @Override
    public PageResp<DailyTrainStationQueryResp> queryList(DailyTrainStationQueryReq req) {
        LambdaQueryWrapper<DailyTrainStation> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(DailyTrainStation::getTrainIndex);
        wrapper.eq(!ObjectUtils.isEmpty(req.getDailyTrainId()),DailyTrainStation::getDailyTrainId,req.getDailyTrainId());

        if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
            log.info("当前页码：{}",req.getCurrentPage());
            log.info("当前页面大小：{}",req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
        }
        List<DailyTrainStation> dailyTrainStations = dailyTrainStationMapper.selectList(wrapper);
        PageInfo<DailyTrainStation> dailyTrainStationPageInfo = new PageInfo<>(dailyTrainStations);
        int pages = dailyTrainStationPageInfo.getPages();
        long total = dailyTrainStationPageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<DailyTrainStationQueryResp> list = new ArrayList<>();
        dailyTrainStations.forEach(dailyTrainStation -> {
            DailyTrainStationQueryResp queryResp = new DailyTrainStationQueryResp();
            BeanUtils.copyProperties(dailyTrainStation,queryResp);
            list.add(queryResp);
        });
        PageResp<DailyTrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return  pageResp;
    }

    @Override
    public void delete(Long id) {
        dailyTrainStationMapper.deleteById(id);
    }
}




