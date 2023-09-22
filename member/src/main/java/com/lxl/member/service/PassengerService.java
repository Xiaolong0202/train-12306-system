package com.lxl.member.service;

import com.lxl.member.req.PassengerSaveOrEditReq;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  15:39
 **/
public interface PassengerService {
    void save(PassengerSaveOrEditReq req);
}
