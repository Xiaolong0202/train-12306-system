package com.lxl.business.service;

import com.lxl.business.domain.TrainToken;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxl.business.req.TrainTokenQueryReq;
import com.lxl.business.resp.TrainTokenQueryResp;
import com.lxl.common.resp.PageResp;

/**
* @author 13430
* @description 针对表【train_token】的数据库操作Service
* @createDate 2023-10-13 22:19:48
*/
public interface TrainTokenService{

    void updateTokenCount(Long id,Integer updateTokenCount);

    PageResp<TrainTokenQueryResp> queryList(TrainTokenQueryReq req);
}
