package com.lxl.business.service;

import com.lxl.business.req.DailyTrainTicketQueryReq;
import com.lxl.business.req.TrainSeatQueryReq;
import com.lxl.business.resp.DailyTrainTicketQueryResp;
import com.lxl.business.resp.TrainSeatQueryResp;
import com.lxl.common.resp.PageResp;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/3  16:08
 **/
public interface DailyTrainTicketService {

    PageResp<DailyTrainTicketQueryResp> queryList(DailyTrainTicketQueryReq req);

    void deleteTicketCache(DailyTrainTicketQueryReq req);
}
