package com.lxl.business.service;

import com.lxl.business.domain.DailyTrainStation;
import com.lxl.business.req.DailyTrainStationQueryReq;
import com.lxl.business.req.DailyTrainStationSaveOrEditReq;
import com.lxl.business.resp.DailyTrainStationQueryResp;
import com.lxl.common.resp.PageResp;

import java.util.List;

/**
* @author 13430
* @description 针对表【train_station(火车车站)】的数据库操作Service
* @createDate 2023-09-26 20:31:27
*/

public interface DailyTrainStationService {
    void save(DailyTrainStationSaveOrEditReq req);

    PageResp<DailyTrainStationQueryResp> queryList(DailyTrainStationQueryReq req);

    void delete(Long id);

    List<DailyTrainStation> getDailyTrainStationsById(Long dailyTrainId);
}
