package com.lxl.business.service;

import com.lxl.business.req.TrainSeatQueryReq;
import com.lxl.business.req.TrainSeatSaveOrEditReq;
import com.lxl.business.resp.TrainSeatQueryResp;
import com.lxl.common.resp.PageResp;

/**
 * @author 13430
 * @description 针对表【trainSeat(火车车站)】的数据库操作Service
 * @createDate 2023-09-26 20:31:27
 */
public interface TrainSeatService {
    void save(TrainSeatSaveOrEditReq req);

    PageResp<TrainSeatQueryResp> queryList(TrainSeatQueryReq req);

    void delete(Long id);
}
