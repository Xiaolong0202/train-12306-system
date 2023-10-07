package com.lxl.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.common.context.MemberInfoContext;
import com.lxl.common.exception.BusinessException;
import com.lxl.common.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.common.req.TicketQueryReq;
import com.lxl.common.req.TicketSaveOrEditReq;
import com.lxl.common.resp.PageResp;
import com.lxl.common.resp.TicketQueryResp;
import com.lxl.common.utils.SnowUtils;
import com.lxl.member.domain.Ticket;
import com.lxl.member.mapper.TicketMapper;
import com.lxl.member.service.TicketService;
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
 * @description 针对表【ticket(车票)】的数据库操作Service实现
 * @createDate 2023-10-07 14:40:16
 */
@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketMapper ticketMapper;

    @Override
    public void save(TicketSaveOrEditReq req) {
        Ticket ticket = BeanUtil.copyProperties(req, Ticket.class);
        Date now = DateTime.now();
        log.info("进行新增");
        //id为空则进行新增
        ticket.setCreateTime(now);
        ticket.setUpdateTime(now);
        ticket.setId(SnowUtils.nextSnowId());

        ticketMapper.insert(ticket);
    }

    @Override
    public PageResp<TicketQueryResp> queryList(TicketQueryReq req) {
        LambdaQueryWrapper<Ticket> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!ObjectUtils.isEmpty(req.getMemberId()), Ticket::getMemberId, req.getMemberId());
        wrapper.orderByDesc(Ticket::getId);

        if (!ObjectUtils.isEmpty(req.getPageSize()) && !ObjectUtils.isEmpty(req.getCurrentPage())) {
            log.info("当前页码：{}", req.getCurrentPage());
            log.info("当前页面大小：{}", req.getPageSize());
            PageHelper.startPage(req.getCurrentPage(), req.getPageSize());
        }
        List<Ticket> tickets = ticketMapper.selectList(wrapper);
        PageInfo<Ticket> ticketPageInfo = new PageInfo<>(tickets);
        int pages = ticketPageInfo.getPages();
        long total = ticketPageInfo.getTotal();
        log.info("总页数 " + pages);
        log.info("总行数 " + total);

        List<TicketQueryResp> list = new ArrayList<>();
        tickets.forEach(ticket -> {
            TicketQueryResp queryResp = new TicketQueryResp();
            BeanUtils.copyProperties(ticket, queryResp);
            list.add(queryResp);
        });
        PageResp<TicketQueryResp> pageResp = new PageResp<>();
        pageResp.setList(list);
        pageResp.setTotal(total);
        return pageResp;
    }
}




