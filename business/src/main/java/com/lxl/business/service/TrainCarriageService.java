package com.lxl.business.service;

import com.lxl.business.req.TrainCarriageQueryReq;
import com.lxl.business.req.TrainCarriageSaveOrEditReq;
import com.lxl.business.resp.TrainCarriageQueryResp;
import com.lxl.common.resp.PageResp;

/**
 * @author 13430
 * @description 针对表【trainCarriage(火车车站)】的数据库操作Service
 * @createDate 2023-09-26 20:31:27
 */

public interface TrainCarriageService {
    void save(TrainCarriageSaveOrEditReq req);

    PageResp<TrainCarriageQueryResp> queryList(TrainCarriageQueryReq req);

    void delete(Long id);
}
