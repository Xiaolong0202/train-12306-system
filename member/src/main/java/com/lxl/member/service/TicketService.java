package com.lxl.member.service;

import com.lxl.common.req.TicketQueryReq;
import com.lxl.common.req.TicketSaveOrEditReq;
import com.lxl.common.resp.PageResp;
import com.lxl.common.resp.TicketQueryResp;
import com.lxl.member.domain.Ticket;
import com.baomidou.mybatisplus.extension.service.IService;



/**
* @author 13430
* @description 针对表【ticket(车票)】的数据库操作Service
* @createDate 2023-10-07 14:40:16
*/
public interface TicketService {

    void save(TicketSaveOrEditReq req);

    PageResp<TicketQueryResp> queryList(TicketQueryReq req);

}
