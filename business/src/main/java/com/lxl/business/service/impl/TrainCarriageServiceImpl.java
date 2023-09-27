package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.TrainCarriage;
import com.lxl.business.mapper.TrainCarriageMapper;
import com.lxl.business.req.TrainCarriageQueryReq;
import com.lxl.business.req.TrainCarriageSaveOrEditReq;
import com.lxl.business.resp.TrainCarriageQueryResp;
import com.lxl.business.service.TrainCarriageService;
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
 * @description 针对表【trainCarriage(车次)】的数据库操作Service实现
 * @createDate 2023-09-26 15:50:20
 */
@Slf4j
@Service
public class TrainCarriageServiceImpl implements TrainCarriageService{
    @Autowired
    TrainCarriageMapper trainCarriageMapper;

    @Override
    public void save(TrainCarriageSaveOrEditReq req) {
        TrainCarriage trainCarriage = BeanUtil.copyProperties(req, TrainCarriage.class);
        Date now = DateTime.now();
        trainCarriage.setUpdateTime(now);

        //检查unique(code,stationName)
        LambdaQueryWrapper<TrainCarriage> lambdaQueryWrapper = new LambdaQueryWrapper<>();


        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(trainCarriage.getTrainIndex()),TrainCarriage::getTrainIndex,trainCarriage.getTrainIndex());
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(trainCarriage.getTrainCode()),TrainCarriage::getTrainCode,trainCarriage.getTrainCode());
        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectList(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(trainCarriages)){
            TrainCarriage station = trainCarriages.get(0);
            if ((station.getTrainIndex().equals(trainCarriage.getTrainIndex())&&station.getTrainCode().equals(station.getTrainCode()))) {
                if (!station.getId().equals(trainCarriage.getId())){
                    //不为空表示已经存在,则抛出异常
                    throw new BusinessException(BussinessExceptionEnum.TRAIN_ALREADY_EXIST);
                }
            }
        }

        if (ObjectUtils.isEmpty(trainCarriage.getId())){
            log.info("进行新增");
            //id为空则进行新增
            trainCarriage.setCreateTime(now);
            trainCarriage.setId(SnowUtils.nextSnowId());

            trainCarriageMapper.insert(trainCarriage);
        }else {
            //id不为空则进行update
            log.info("进行update");
            trainCarriage.setCreateTime(null);
            trainCarriageMapper.updateById(trainCarriage);
        }

    }

    @Override
    public PageResp<TrainCarriageQueryResp> queryList(TrainCarriageQueryReq req) {
        LambdaQueryWrapper<TrainCarriage> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(TrainCarriage::getId);

        if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
            log.info("当前页码：{}",req.getCurrentPage());
            log.info("当前页面大小：{}",req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
        }
        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectList(wrapper);
        PageInfo<TrainCarriage> trainCarriagePageInfo = new PageInfo<>(trainCarriages);
        int pages = trainCarriagePageInfo.getPages();
        long total = trainCarriagePageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<TrainCarriageQueryResp> list = new ArrayList<>();
        trainCarriages.forEach(trainCarriage -> {
            TrainCarriageQueryResp queryResp = new TrainCarriageQueryResp();
            BeanUtils.copyProperties(trainCarriage,queryResp);
            list.add(queryResp);
        });
        PageResp<TrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return  pageResp;
    }

    @Override
    public void delete(Long id) {
        trainCarriageMapper.deleteById(id);
    }
}




