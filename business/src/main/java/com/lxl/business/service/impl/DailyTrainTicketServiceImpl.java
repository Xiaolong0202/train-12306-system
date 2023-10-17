package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.DailyTrainTicket;
import com.lxl.business.mapper.DailyTrainTicketMapper;
import com.lxl.business.req.DailyTrainTicketQueryReq;
import com.lxl.business.resp.DailyTrainTicketQueryResp;
import com.lxl.business.resp.TrainSeatQueryResp;
import com.lxl.business.service.DailyTrainTicketService;
import com.lxl.common.resp.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/3  16:09
 **/
@Service
@Slf4j
public class DailyTrainTicketServiceImpl implements DailyTrainTicketService {

    @Autowired
    DailyTrainTicketMapper dailyTrainTicketMapper;


    /**
     * 更新车票信息的缓存缓存
     * @param req
     * @return
     */
    @CachePut(cacheNames = "DailyTrainTicketServiceImpl.queryList")
    public PageResp<DailyTrainTicketQueryResp> updateDailyTrainTicket(DailyTrainTicketQueryReq req){
        log.info("更新缓存！");
        return queryList(req);
    }


    @Cacheable(cacheNames = "DailyTrainTicketServiceImpl.queryList")
    @Override
    public PageResp<DailyTrainTicketQueryResp> queryList(DailyTrainTicketQueryReq req) {
        //TODO 可以实现分布式锁防止缓存击穿的问题
        log.info("NO CACHING!!");
//        dailyTrainTicketLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(req.getDailyTrainId()),DailyTrainTicket::getDailyTrainId,);
//        dailyTrainTicketLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(req.getStart()),DailyTrainTicket::getStart,);
//        dailyTrainTicketLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(req.getEnd()),DailyTrainTicket::getEnd,);
//        dailyTrainTicketLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(req.getStartDate()),DailyTrainTicket::getStartDate,);



        if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
            log.info("当前页码：{}",req.getCurrentPage());
            log.info("当前页面大小：{}",req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
        }

        List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketMapper.selectListAndIncludeDailyTrain(req.getDailyTrainId(),req.getStart(),req.getEnd(),req.getStartDate());
        log.info("得到合适的车票信息:"+dailyTrainTickets);
        PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(dailyTrainTickets);
        int pages = pageInfo.getPages();
        long total = pageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<DailyTrainTicketQueryResp> list = new ArrayList<>();

        dailyTrainTickets.forEach(dailyTrainTicket -> {
            DailyTrainTicketQueryResp dailyTrainTicketQueryResp = BeanUtil.copyProperties(dailyTrainTicket, DailyTrainTicketQueryResp.class);
            list.add(dailyTrainTicketQueryResp);
        });

        PageResp<DailyTrainTicketQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);

        return pageResp;
    }
}
