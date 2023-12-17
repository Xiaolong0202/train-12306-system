package com.lxl.business.controller.web;

import com.lxl.business.req.DailyTrainTicketQueryReq;
import com.lxl.business.req.WebDailyTrainTicketQueryReq;
import com.lxl.business.service.DailyTrainTicketService;
import com.lxl.common.resp.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/3  23:35
 **/
@RequestMapping("/ticket")
@RestController
public class TicketController {

    @Autowired
    DailyTrainTicketService dailyTrainTicketService;

    @GetMapping("/query-list")
    public CommonResp<?> queryList(WebDailyTrainTicketQueryReq webDailyTrainTicketQueryReq){
        return CommonResp.buildSuccess(dailyTrainTicketService.queryWebList(webDailyTrainTicketQueryReq),"查询成功");
    }
}
