package com.lxl.member.service;

import com.lxl.member.req.PassengerQueryReq;
import com.lxl.member.req.PassengerSaveOrEditReq;
import com.lxl.member.resp.PassengerQueryResp;

import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  15:39
 **/
public interface PassengerService {
    void save(PassengerSaveOrEditReq req);

    List<PassengerQueryResp> queryList(PassengerQueryReq req);
}
