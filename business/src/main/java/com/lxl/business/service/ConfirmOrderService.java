package com.lxl.business.service;

import com.lxl.business.dto.ConfirmOrderMQDTO;
import com.lxl.business.req.ConfirmOrderQueryReq;
import com.lxl.business.resp.ConfirmOrderQueryResp;
import com.lxl.common.resp.PageResp;

/**
* @author 13430
* @description 针对表【confirm_order(确认订单)】的数据库操作Service
* @createDate 2023-10-05 11:47:00
*/
public interface ConfirmOrderService  {
    PageResp<ConfirmOrderQueryResp> queryList(ConfirmOrderQueryReq req);

    void doConfirm(ConfirmOrderMQDTO req);

    Integer queryOrderQueueStatus(Long confirmOrderId);
}
