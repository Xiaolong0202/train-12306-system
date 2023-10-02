package com.lxl.business.service;

import com.lxl.business.req.DailyTrainCarriageQueryReq;
import com.lxl.business.req.DailyTrainCarriageSaveOrEditReq;
import com.lxl.business.resp.DailyTrainCarriageQueryResp;
import com.lxl.common.resp.PageResp;

/**
 * @author 13430
 * @description 针对表【trainCarriage(火车车站)】的数据库操作Service
 * @createDate 2023-09-26 20:31:27
 */

public interface DailyTrainCarriageService {
    void save(DailyTrainCarriageSaveOrEditReq req);

    PageResp<DailyTrainCarriageQueryResp> queryList(DailyTrainCarriageQueryReq req);

    void delete(Long id);
}
