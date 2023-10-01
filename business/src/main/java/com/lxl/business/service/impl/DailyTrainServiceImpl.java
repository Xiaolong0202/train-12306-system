package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.DailyTrain;

import com.lxl.business.mapper.DailyTrainMapper;
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
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            if (!ObjectUtils.nullSafeEquals(dailyTrains.get(0).getId(), dailyTrain.getId())){
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

        wrapper.eq(!ObjectUtils.isEmpty(req.getStartDate()),DailyTrain::getStartDate,req.getStartDate());

        wrapper.eq(!ObjectUtils.isEmpty(req.getCode()),DailyTrain::getCode,req.getCode());

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

}




