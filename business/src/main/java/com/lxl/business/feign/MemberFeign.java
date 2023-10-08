package com.lxl.business.feign;

import com.lxl.common.req.TicketQueryReq;
import com.lxl.common.req.TicketSaveOrEditReq;
import com.lxl.common.resp.CommonResp;
import com.lxl.common.resp.PageResp;
import com.lxl.common.resp.TicketQueryResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/7  15:18
 **/
@FeignClient("member")
//@FeignClient(name = "member" ,url = "http://127.0.0.1:8001")
public interface MemberFeign {

    @GetMapping("/member/ticket/query-list")
    public CommonResp<?> queryList(TicketQueryReq ticketQueryReq);

    @PostMapping("/member/ticket/save")
    public CommonResp<?> save(@RequestBody TicketSaveOrEditReq ticketSaveOrEditReq);
}
