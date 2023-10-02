package com.lxl.business.service;

import com.lxl.business.domain.DailyTrain;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxl.business.req.DailyTrainQueryReq;
import com.lxl.business.req.DailyTrainSaveOrEditReq;
import com.lxl.business.resp.DailyTrainQueryResp;
import com.lxl.common.resp.PageResp;

import java.util.Date;

/**
* @author 13430
* @description 针对表【daily_train(每日车次)】的数据库操作Service
* @createDate 2023-10-01 13:49:18
*/
public interface DailyTrainService{

    void save(DailyTrainSaveOrEditReq req);

    PageResp<DailyTrainQueryResp> queryList(DailyTrainQueryReq req);

    void delete(Long id);

    DailyTrainQueryResp queryOne(Long trainId);

    void genDaily(Date date);
}
