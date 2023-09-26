package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.Station;
import com.lxl.business.req.StationQueryReq;
import com.lxl.business.req.StationSaveOrEditReq;
import com.lxl.business.resp.StationQueryResp;
import com.lxl.business.service.StationService;
import com.lxl.business.mapper.StationMapper;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.resp.PageResp;
import com.lxl.common.utils.SnowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author 13430
* @description 针对表【station】的数据库操作Service实现
* @createDate 2023-09-25 21:24:35
*/
@Slf4j
@Service
public class StationServiceImpl implements StationService{

    @Autowired
    StationMapper stationMapper;

    @Override
    public void save(StationSaveOrEditReq req) {
        Station station = BeanUtil.copyProperties(req, Station.class);
        Date now = DateTime.now();
        station.setUpdateTime(now);

        boolean isNameUpdate = true;

        if (!ObjectUtils.isEmpty(station.getId())){
            //如果有id
            isNameUpdate = !station.getName().equals(stationMapper.selectNameById(station.getId()));
        }

        if (isNameUpdate){
            //如果name被更改了,实现unique约束
            LambdaQueryWrapper<Station> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(station.getName()),Station::getName,station.getName());
            List<Station> stations = stationMapper.selectList(lambdaQueryWrapper);
            if (CollUtil.isNotEmpty(stations)){
                //不为空表示已经存在,则抛出异常
                throw new BusinessException(BussinessExceptionEnum.STATION_ALREADY_EXIST);
            }
        }

        if (ObjectUtils.isEmpty(station.getId())){
            log.info("进行新增");
            //id为空则进行新增
            station.setCreateTime(now);
            station.setId(SnowUtils.nextSnowId());

            stationMapper.insert(station);
        }else {
            //id不为空则进行update
            log.info("进行update");
            station.setCreateTime(null);
            stationMapper.updateById(station);
        }

    }

    @Override
    public PageResp<StationQueryResp> queryList(StationQueryReq req) {
        LambdaQueryWrapper<Station> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Station::getId);

        if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
            log.info("当前页码：{}",req.getCurrentPage());
            log.info("当前页面大小：{}",req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
        }
        List<Station> stations = stationMapper.selectList(wrapper);
        PageInfo<Station> stationPageInfo = new PageInfo<>(stations);
        int pages = stationPageInfo.getPages();
        long total = stationPageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<StationQueryResp> list = new ArrayList<>();
        stations.forEach(station -> {
            StationQueryResp queryResp = new StationQueryResp();
            BeanUtils.copyProperties(station,queryResp);
            list.add(queryResp);
        });
        PageResp<StationQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return  pageResp;
    }

    @Override
    public void delete(Long id) {
        stationMapper.deleteById(id);
    }

}




