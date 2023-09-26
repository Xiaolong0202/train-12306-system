package com.lxl.business.service;

import com.lxl.business.req.TrainStationQueryReq;
import com.lxl.business.req.TrainStationSaveOrEditReq;
import com.lxl.business.resp.TrainStationQueryResp;
import com.lxl.common.resp.PageResp;

/**
* @author 13430
* @description 针对表【train_station(火车车站)】的数据库操作Service
* @createDate 2023-09-26 20:31:27
*/

public interface TrainStationService {
    void save(TrainStationSaveOrEditReq req);

    PageResp<TrainStationQueryResp> queryList(TrainStationQueryReq req);

    void delete(Long id);
}
