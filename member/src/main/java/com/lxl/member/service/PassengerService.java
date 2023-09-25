package com.lxl.member.service;

import com.lxl.member.req.passenger.PassengerQueryReq;
import com.lxl.member.req.passenger.PassengerSaveOrEditReq;
import com.lxl.member.resp.passenger.PassengerQueryResp;
import com.lxl.resp.PageResp;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  15:39
 **/
public interface PassengerService {
    void save(PassengerSaveOrEditReq req);

    PageResp<PassengerQueryResp> queryList(PassengerQueryReq req);

    void delete(Long id);
}
