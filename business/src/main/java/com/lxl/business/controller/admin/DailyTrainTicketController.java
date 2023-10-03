package com.lxl.business.controller.admin;

import com.lxl.business.req.DailyTrainTicketQueryReq;
import com.lxl.business.service.DailyTrainTicketService;
import com.lxl.common.resp.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/3  16:04
 **/
@RequestMapping("/dailyTrainTicket/admin")
@RestController
public class DailyTrainTicketController {

    @Autowired
    DailyTrainTicketService dailyTrainTicketService;

    @GetMapping("/query-list")
    public CommonResp<?> queryList(DailyTrainTicketQueryReq dailyTrainTicketQueryReq){
        return CommonResp.buildSuccess(dailyTrainTicketService.queryList(dailyTrainTicketQueryReq),"查询成功");
    }

}
