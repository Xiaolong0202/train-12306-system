package com.lxl.business.service;

import com.lxl.business.req.DailyTrainSeatQueryReq;
import com.lxl.business.req.DailyTrainSeatSaveOrEditReq;
import com.lxl.business.resp.DailyTrainSeatQueryResp;
import com.lxl.common.resp.PageResp;

import java.util.List;

/**
 * @author 13430
 * @description 针对表【dailyTrainSeat(火车车站)】的数据库操作Service
 * @createDate 2023-09-26 20:31:27
 */
public interface DailyTrainSeatService {
    void save(DailyTrainSeatSaveOrEditReq req);

    PageResp<DailyTrainSeatQueryResp> queryList(DailyTrainSeatQueryReq req);

    void delete(Long id);

    List<DailyTrainSeatQueryResp> selectListByDailyTrainId(Long dailyTrainId);
}
