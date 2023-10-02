package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.DailyTrainCarriage;
import com.lxl.business.enums.SeatColTypeEnum;
import com.lxl.business.mapper.DailyTrainCarriageMapper;
import com.lxl.business.req.DailyTrainCarriageQueryReq;
import com.lxl.business.req.DailyTrainCarriageSaveOrEditReq;
import com.lxl.business.resp.DailyTrainCarriageQueryResp;
import com.lxl.business.service.DailyTrainCarriageService;
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
 * @description 针对表【dailyTrainCarriage(车次)】的数据库操作Service实现
 * @createDate 2023-09-26 15:50:20
 */
@Slf4j
@Service
public class DailyTrainCarriageServiceImpl implements DailyTrainCarriageService{
    @Autowired
    DailyTrainCarriageMapper dailyTrainCarriageMapper;

    @Override
    public void save(DailyTrainCarriageSaveOrEditReq req) {
        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(req, DailyTrainCarriage.class);
        Date now = DateTime.now();
        dailyTrainCarriage.setUpdateTime(now);
        //根据车厢的类型与排数计算列数与座位总数
        dailyTrainCarriage.setColumnCount(SeatColTypeEnum.getSeatCols(dailyTrainCarriage.getSeatType()).size());
        dailyTrainCarriage.setSeatCount(dailyTrainCarriage.getRowCount()*dailyTrainCarriage.getColumnCount());

        //检查unique(DailyTrainId,stationName)
        LambdaQueryWrapper<DailyTrainCarriage> lambdaQueryWrapper = new LambdaQueryWrapper<>();


        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainCarriage.getTrainIndex()),DailyTrainCarriage::getTrainIndex,dailyTrainCarriage.getTrainIndex());
        lambdaQueryWrapper.eq(!ObjectUtils.isEmpty(dailyTrainCarriage.getDailyTrainId()),DailyTrainCarriage::getDailyTrainId,dailyTrainCarriage.getDailyTrainId());
        List<DailyTrainCarriage> dailyTrainCarriages = dailyTrainCarriageMapper.selectList(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(dailyTrainCarriages)){
            DailyTrainCarriage station = dailyTrainCarriages.get(0);
            if ((station.getTrainIndex().equals(dailyTrainCarriage.getTrainIndex())&&station.getDailyTrainId().equals(station.getDailyTrainId()))) {
                if (!station.getId().equals(dailyTrainCarriage.getId())){
                    //不为空表示已经存在,则抛出异常
                    throw new BusinessException(BussinessExceptionEnum.TRAIN_CARRIAGE_ALREADY_EXIST);
                }
            }
        }

        if (ObjectUtils.isEmpty(dailyTrainCarriage.getId())){
            log.info("进行新增");
            //id为空则进行新增
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setId(SnowUtils.nextSnowId());

            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        }else {
            //id不为空则进行update
            log.info("进行update");
            dailyTrainCarriage.setCreateTime(null);
            dailyTrainCarriageMapper.updateById(dailyTrainCarriage);
        }

    }

    @Override
    public PageResp<DailyTrainCarriageQueryResp> queryList(DailyTrainCarriageQueryReq req) {
        LambdaQueryWrapper<DailyTrainCarriage> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(DailyTrainCarriage::getTrainIndex);
        wrapper.eq(!ObjectUtils.isEmpty(req.getDailyTrainId()), DailyTrainCarriage::getDailyTrainId,req.getDailyTrainId());

        if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
            log.info("当前页码：{}",req.getCurrentPage());
            log.info("当前页面大小：{}",req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
        }
        List<DailyTrainCarriage> dailyTrainCarriages = dailyTrainCarriageMapper.selectList(wrapper);
        PageInfo<DailyTrainCarriage> dailyTrainCarriagePageInfo = new PageInfo<>(dailyTrainCarriages);
        int pages = dailyTrainCarriagePageInfo.getPages();
        long total = dailyTrainCarriagePageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<DailyTrainCarriageQueryResp> list = new ArrayList<>();
        dailyTrainCarriages.forEach(dailyTrainCarriage -> {
            DailyTrainCarriageQueryResp queryResp = new DailyTrainCarriageQueryResp();
            BeanUtils.copyProperties(dailyTrainCarriage,queryResp);
            list.add(queryResp);
        });
        PageResp<DailyTrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return  pageResp;
    }

    @Override
    public void delete(Long id) {
        dailyTrainCarriageMapper.deleteById(id);
    }
}




