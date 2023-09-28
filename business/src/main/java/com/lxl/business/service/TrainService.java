package com.lxl.business.service;

import com.lxl.business.domain.Train;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxl.business.req.TrainQueryReq;
import com.lxl.business.req.TrainSaveOrEditReq;
import com.lxl.business.resp.TrainQueryResp;
import com.lxl.common.resp.PageResp;


public interface TrainService {
    void save(TrainSaveOrEditReq req);

    PageResp<TrainQueryResp> queryList(TrainQueryReq req);

    void delete(Long id);

    TrainQueryResp queryOne(Long trainId);
}
