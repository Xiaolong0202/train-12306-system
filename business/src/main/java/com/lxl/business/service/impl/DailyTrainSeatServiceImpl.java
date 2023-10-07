package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.DailyTrainSeat;
import com.lxl.business.mapper.DailyTrainSeatMapper;
import com.lxl.business.req.DailyTrainSeatQueryReq;
import com.lxl.business.req.DailyTrainSeatSaveOrEditReq;
import com.lxl.business.resp.DailyTrainSeatQueryResp;
import com.lxl.business.service.DailyTrainSeatService;
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
 * @description 针对表【dailyTrainSeat(车次)】的数据库操作Service实现
 * @createDate 2023-09-26 15:50:20
 */
@Slf4j
@Service
public class DailyTrainSeatServiceImpl implements DailyTrainSeatService{
    @Autowired
    DailyTrainSeatMapper dailyTrainSeatMapper;

    @Override
    public void save(DailyTrainSeatSaveOrEditReq req) {
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class);
        Date now = DateTime.now();
        dailyTrainSeat.setUpdateTime(now);


        if (ObjectUtils.isEmpty(dailyTrainSeat.getId())){
            log.info("进行新增");
            //id为空则进行新增
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setId(SnowUtils.nextSnowId());

            dailyTrainSeatMapper.insert(dailyTrainSeat);
        }else {
            //id不为空则进行update
            log.info("进行update");
            dailyTrainSeat.setCreateTime(null);
            dailyTrainSeatMapper.updateById(dailyTrainSeat);
        }

    }

    @Override
    public PageResp<DailyTrainSeatQueryResp> queryList(DailyTrainSeatQueryReq req) {
        LambdaQueryWrapper<DailyTrainSeat> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(DailyTrainSeat::getCarriageId);
        wrapper.orderByAsc(DailyTrainSeat::getCarriageSeatIndex);
        wrapper.eq(!ObjectUtils.isEmpty(req.getDailyTrainId()), DailyTrainSeat::getDailyTrainId,req.getDailyTrainId());

        if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
            log.info("当前页码：{}",req.getCurrentPage());
            log.info("当前页面大小：{}",req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
        }
        List<DailyTrainSeat> dailyTrainSeats = dailyTrainSeatMapper.selectList(wrapper);
        PageInfo<DailyTrainSeat> dailyTrainSeatPageInfo = new PageInfo<>(dailyTrainSeats);
        int pages = dailyTrainSeatPageInfo.getPages();
        long total = dailyTrainSeatPageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<DailyTrainSeatQueryResp> list = new ArrayList<>();
        dailyTrainSeats.forEach(dailyTrainSeat -> {
            DailyTrainSeatQueryResp queryResp = new DailyTrainSeatQueryResp();
            BeanUtils.copyProperties(dailyTrainSeat,queryResp);
            list.add(queryResp);
        });
        PageResp<DailyTrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return  pageResp;
    }

    @Override
    public void delete(Long id) {
        dailyTrainSeatMapper.deleteById(id);
    }
}




