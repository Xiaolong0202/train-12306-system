package com.lxl.member.controller;

import com.lxl.common.req.TicketQueryReq;
import com.lxl.common.req.TicketSaveOrEditReq;
import com.lxl.common.resp.CommonResp;
import com.lxl.common.resp.PageResp;
import com.lxl.common.resp.TicketQueryResp;
import com.lxl.member.service.TicketService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/7  15:09
 **/
@RequestMapping("/ticket")
@RestController()
public class TicketController {

    @Autowired
    TicketService ticketService;


    @GetMapping("/query-list")
    public CommonResp<?> queryList(TicketQueryReq ticketQueryReq){
        PageResp<TicketQueryResp> ticketQueryRespPageResp = ticketService.queryList(ticketQueryReq);
        return CommonResp.buildSuccess(ticketQueryRespPageResp,"查询成功");
    }


    @PostMapping("/save")
    public CommonResp<?> save(@RequestBody TicketSaveOrEditReq ticketSaveOrEditReq){
        ticketService.save(ticketSaveOrEditReq);
//        try {
//            Thread.sleep(1000*10*10*10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        if (1==1){
//            throw new RuntimeException("test Seata 异常");
//        }
        return CommonResp.buildSuccess("保存成功");
    }
}
