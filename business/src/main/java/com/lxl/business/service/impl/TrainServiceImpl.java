package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.Train;
import com.lxl.business.domain.Train;
import com.lxl.business.domain.TrainCarriage;
import com.lxl.business.domain.TrainSeat;
import com.lxl.business.enums.SeatColTypeEnum;
import com.lxl.business.mapper.TrainCarriageMapper;
import com.lxl.business.mapper.TrainMapper;
import com.lxl.business.mapper.TrainSeatMapper;
import com.lxl.business.req.TrainQueryReq;
import com.lxl.business.req.TrainSaveOrEditReq;
import com.lxl.business.req.TrainQueryReq;
import com.lxl.business.req.TrainSaveOrEditReq;
import com.lxl.business.resp.TrainQueryResp;
import com.lxl.business.resp.TrainQueryResp;
import com.lxl.business.service.TrainService;
import com.lxl.business.mapper.TrainMapper;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.resp.PageResp;
import com.lxl.common.utils.SnowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author 13430
* @description 针对表【train(车次)】的数据库操作Service实现
* @createDate 2023-09-26 15:50:20
*/
@Slf4j
@Service
public class TrainServiceImpl implements TrainService{
    @Autowired
    TrainMapper trainMapper;
    @Autowired
    TrainCarriageMapper trainCarriageMapper;
    @Autowired
    TrainSeatMapper trainSeatMapper;

    @Override
    public void save(TrainSaveOrEditReq req) {
        Train train = BeanUtil.copyProperties(req, Train.class);
        Date now = DateTime.now();
        train.setUpdateTime(now);

        boolean isNameUpdate = true;

        if (!ObjectUtils.isEmpty(train.getId())){
            //如果有id
            isNameUpdate = !train.getCode().equals(trainMapper.selectCodeById(train.getId()));
        }

        if (isNameUpdate){
            //如果name被更改了,实现unique约束
            LambdaQueryWrapper<Train> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(train.getCode()),Train::getCode,train.getCode());
            List<Train> trains = trainMapper.selectList(lambdaQueryWrapper);
            if (CollUtil.isNotEmpty(trains)){
                //不为空表示已经存在,则抛出异常
                throw new BusinessException(BussinessExceptionEnum.TRAIN_ALREADY_EXIST);
            }
        }

        if (ObjectUtils.isEmpty(train.getId())){
            log.info("进行新增");
            //id为空则进行新增
            train.setCreateTime(now);
            train.setId(SnowUtils.nextSnowId());

            trainMapper.insert(train);
        }else {
            //id不为空则进行update
            log.info("进行update");
            train.setCreateTime(null);
            trainMapper.updateById(train);
        }

    }

    @Override
    public PageResp<TrainQueryResp> queryList(TrainQueryReq req) {
        LambdaQueryWrapper<Train> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Train::getId);

        if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
            log.info("当前页码：{}",req.getCurrentPage());
            log.info("当前页面大小：{}",req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
        }
        List<Train> trains = trainMapper.selectList(wrapper);
        PageInfo<Train> trainPageInfo = new PageInfo<>(trains);
        int pages = trainPageInfo.getPages();
        long total = trainPageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<TrainQueryResp> list = new ArrayList<>();
        trains.forEach(train -> {
            TrainQueryResp queryResp = new TrainQueryResp();
            BeanUtils.copyProperties(train,queryResp);
            list.add(queryResp);
        });
        PageResp<TrainQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return  pageResp;
    }

    @Override
    public void delete(Long id) {
        trainMapper.deleteById(id);
    }

    @Override
    public TrainQueryResp queryOne(Long trainId) {
        LambdaQueryWrapper<Train> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(trainId),Train::getId,trainId);
        Train train = trainMapper.selectOne(lambdaQueryWrapper);
        return BeanUtil.copyProperties(train,TrainQueryResp.class);
    }

    /**
     * 自动生成座位
     * @param trainId
     */
    @Transactional
    @Override
    public void genTrainSeat(Long trainId) {
        //先将之前所有的Seats进行删除
        LambdaQueryWrapper<TrainSeat> seatDelMapper= new LambdaQueryWrapper<>();
        seatDelMapper.eq(!ObjectUtils.isEmpty(trainId),TrainSeat::getTrainId,trainId);
        trainSeatMapper.delete(seatDelMapper);
        //先获取所有的车厢
        LambdaQueryWrapper<TrainCarriage> trainCarriageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        trainCarriageLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(trainId),TrainCarriage::getTrainId,trainId);
        List<TrainCarriage> carriages = trainCarriageMapper.selectList(trainCarriageLambdaQueryWrapper);

        for (TrainCarriage carriage : carriages) {
            int count = 1;
            Integer rowCount = carriage.getRowCount();
            List<SeatColTypeEnum> seatCols = SeatColTypeEnum.getSeatCols(carriage.getSeatType());
            log.info("当前车次所有的车厢数目为：{}",seatCols.size());
            for (int i = 1; i <= rowCount; i++) {
                for (SeatColTypeEnum seatCol : seatCols) {
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setTrainId(trainId);
                    trainSeat.setCarriageId(carriage.getId());
                    trainSeat.setSeatRow(String.format("%02d",i));
                    trainSeat.setSeatCol(seatCol.code);
                    trainSeat.setId(SnowUtils.nextSnowId());
                    trainSeat.setCarriageSeatIndex(count++);
                    trainSeat.setSeatType(carriage.getSeatType());

                    Date now = new Date(System.currentTimeMillis());
                    trainSeat.setCreateTime(now);
                    trainSeat.setUpdateTime(now);

                    trainSeatMapper.insert(trainSeat);
                }
            }
        }
    }
}




