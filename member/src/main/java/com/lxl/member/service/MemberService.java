package com.lxl.member.service;

import com.lxl.member.req.member.MemberLoginReq;
import com.lxl.resp.MemberLoginResp;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  20:41
 **/
public interface MemberService {

    int count();

    long register(String member);

    void sendCode(String mobile);

    MemberLoginResp login(MemberLoginReq req);
}
