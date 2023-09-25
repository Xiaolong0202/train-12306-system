package com.lxl.business.service;

import com.lxl.business.req.StationQueryReq;
import com.lxl.business.req.StationSaveOrEditReq;
import com.lxl.business.resp.StationQueryResp;
import com.lxl.common.resp.PageResp;

/**
* @author 13430
* @description 针对表【station】的数据库操作Service
* @createDate 2023-09-25 21:24:35
*/
public interface StationService {
    void save(StationSaveOrEditReq req);

    PageResp<StationQueryResp> queryList(StationQueryReq req);

    void delete(Long id);
}
