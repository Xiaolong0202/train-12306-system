package com.lxl.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.DailyTrainTicket;
import com.lxl.business.mapper.DailyTrainTicketMapper;
import com.lxl.business.req.DailyTrainTicketQueryReq;
import com.lxl.business.req.WebDailyTrainTicketQueryReq;
import com.lxl.business.resp.DailyTrainTicketQueryResp;
import com.lxl.business.resp.TrainSeatQueryResp;
import com.lxl.business.service.DailyTrainTicketService;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.resp.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    DailyTrainTicketService dailyTrainTicketService; //将自身注入才能使用缓存

    private static final String DAILY_TRAIN_CACHE_REDISSON_KEY_PREFIX = "DAILY_TRAIN_CACHE_REDISSON_KEY_PREFIX";

    /**
     * 删除车票信息的缓存缓存
     *
     * @param ticketQueryReq
     * @return
     */
    @Override
    @CacheEvict(cacheNames = "DailyTrainTicketServiceImpl.queryList", beforeInvocation = true)//执行该方法之前先删除缓存
    public void deleteTicketCache(WebDailyTrainTicketQueryReq ticketQueryReq) {
        log.info("删除查询条件【{}】缓存！", ticketQueryReq.toString());
    }

    /**
     * 从admin中来查有分页 没有缓存
     * @param req
     * @return
     */
    @Override
    public PageResp<DailyTrainTicketQueryResp> queryAdminList(DailyTrainTicketQueryReq req) {

//        dailyTrainTicketLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(req.getDailyTrainId()),DailyTrainTicket::getDailyTrainId,);
//        dailyTrainTicketLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(req.getStart()),DailyTrainTicket::getStart,);
//        dailyTrainTicketLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(req.getEnd()),DailyTrainTicket::getEnd,);
//        dailyTrainTicketLambdaQueryWrapper.eq(!ObjectUtils.isEmpty(req.getStartDate()),DailyTrainTicket::getStartDate,);


        if (!ObjectUtils.isEmpty(req.getPageSize()) && !ObjectUtils.isEmpty(req.getCurrentPage())) {
            log.info("当前页码：{}", req.getCurrentPage());
            log.info("当前页面大小：{}", req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(), req.getPageSize());
        }

        List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketMapper.selectListAndIncludeDailyTrain(req.getDailyTrainId(), req.getStart(), req.getEnd(), req.getStartDate());
        log.info("得到合适的车票信息:" + dailyTrainTickets);
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

    /**
     *  用户端查询 有缓存 没有分页
     * @param req
     * @return
     */
    @Override
    @Cacheable(cacheNames = "DailyTrainTicketServiceImpl.queryList")
    public PageResp<DailyTrainTicketQueryResp> queryWebList(WebDailyTrainTicketQueryReq req){
//        没有获取到缓存说明
        log.info("车票【{}】查询没有获取到缓存",req.toString());
        RLock lock = redissonClient.getLock(DAILY_TRAIN_CACHE_REDISSON_KEY_PREFIX + ':' + req.getStart() + ':' + req.getEnd() + ':' + req.getStartDate().toString());
        lock.lock(); //持续阻塞直到获取分布式锁
        try {
//            获得分布式锁之后，判断是否有缓存，没有缓存就从数据库中查，有缓存就直接返回缓存中的结果
            PageResp<DailyTrainTicketQueryResp> dailyTrainTicketQueryRespPageResp = dailyTrainTicketService.queryWebCache(req);
            if (dailyTrainTicketQueryRespPageResp!=null){
                log.info("车票【{}】查询获取了分布式锁，但是缓存已经被更新，直接返回缓存",req.toString());
                return dailyTrainTicketQueryRespPageResp;
            }
            log.info("车票【{}】查询获取了分布式锁，并且缓存没有值，将从数据库当中获取数据构建缓存",req.toString());
            //没有缓存的话，则需要从数据库中查并放入缓存当中
            List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketMapper.selectListAndIncludeDailyTrain(null, req.getStart(), req.getEnd(), req.getStartDate());
            log.info("得到合适的车票信息:" + dailyTrainTickets);
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
        }finally {
            //不管怎么样都养释放锁
            lock.unlock();
            log.info("车票【{}】释放分布式锁",req.toString());
        }
    }

    @Override
    @Cacheable(cacheNames = "DailyTrainTicketServiceImpl.queryList")
    public PageResp<DailyTrainTicketQueryResp> queryWebCache(WebDailyTrainTicketQueryReq req){
        return null;
    }
}
