package com.lxl.business.service;

import com.lxl.business.req.DailyTrainTicketQueryReq;
import com.lxl.business.req.WebDailyTrainTicketQueryReq;
import com.lxl.business.resp.DailyTrainTicketQueryResp;
import com.lxl.common.resp.PageResp;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/3  16:08
 **/
public interface DailyTrainTicketService {

    PageResp<DailyTrainTicketQueryResp> queryAdminList(DailyTrainTicketQueryReq req);

    PageResp<DailyTrainTicketQueryResp> queryWebList(WebDailyTrainTicketQueryReq req);
    PageResp<DailyTrainTicketQueryResp> queryWebCache(WebDailyTrainTicketQueryReq req);

    void deleteTicketCache(WebDailyTrainTicketQueryReq req);
}
