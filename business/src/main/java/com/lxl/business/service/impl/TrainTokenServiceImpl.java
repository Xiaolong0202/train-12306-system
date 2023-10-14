package com.lxl.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxl.business.domain.TrainToken;
import com.lxl.business.req.TrainTokenQueryReq;
import com.lxl.business.resp.TrainTokenQueryResp;
import com.lxl.business.service.TrainTokenService;
import com.lxl.business.mapper.TrainTokenMapper;
import com.lxl.common.resp.PageResp;
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
* @description 针对表【train_token】的数据库操作Service实现
* @createDate 2023-10-13 22:19:48
*/
@Slf4j
@Service
public class TrainTokenServiceImpl  implements TrainTokenService{

    @Autowired
    TrainTokenMapper trainTokenMapper;

    @Override
    public void updateTokenCount(Long id,Integer updateTokenCount) {
        TrainToken trainToken = new TrainToken();
        trainToken.setId(id);
        trainToken.setTokenCount(updateTokenCount);
        trainToken.setUpdateTime(new Date());
        trainTokenMapper.updateById(trainToken);
    }
    @Override
    public PageResp<TrainTokenQueryResp> queryList(TrainTokenQueryReq req) {
            LambdaQueryWrapper<TrainToken> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(TrainToken::getUpdateTime);
            wrapper.eq(!ObjectUtils.isEmpty(req.getDailyTrainId()), TrainToken::getDailyTrainId,req.getDailyTrainId());
            wrapper.eq(!ObjectUtils.isEmpty(req.getStartDate()), TrainToken::getStartDate,req.getStartDate());

            if (!ObjectUtils.isEmpty(req.getPageSize())&&!ObjectUtils.isEmpty(req.getCurrentPage())){
                log.info("当前页码：{}",req.getCurrentPage());
                log.info("当前页面大小：{}",req.getPageSize());
                PageHelper.startPage(req.getCurrentPage(),req.getPageSize());
            }
            List<TrainToken> trainTokens = trainTokenMapper.selectList(wrapper);
            PageInfo<TrainToken> trainTokenPageInfo = new PageInfo<>(trainTokens);
            int pages = trainTokenPageInfo.getPages();
            long total = trainTokenPageInfo.getTotal();
            log.info("总页数 " + pages);
            log.info("总行数 " + total);

            List<TrainTokenQueryResp> list = new ArrayList<>();
            trainTokens.forEach(trainToken -> {
                TrainTokenQueryResp queryResp = new TrainTokenQueryResp();
                BeanUtils.copyProperties(trainToken,queryResp);
                list.add(queryResp);
            });
            PageResp<TrainTokenQueryResp> pageResp = new PageResp<>();
            pageResp.setList(list);
            pageResp.setTotal(total);
            return  pageResp;
    }
}




