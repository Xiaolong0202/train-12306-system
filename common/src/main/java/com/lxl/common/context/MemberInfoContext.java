package com.lxl.common.context;

import com.lxl.common.resp.MemberLoginResp;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  23:36
 **/
@Slf4j
public class MemberInfoContext {
    private static final ThreadLocal<MemberLoginResp> MEMBER_LOGIN_RESP_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(MemberLoginResp memberLoginResp){
        MEMBER_LOGIN_RESP_THREAD_LOCAL.set(memberLoginResp);
    }

    public static MemberLoginResp get(){
     return MEMBER_LOGIN_RESP_THREAD_LOCAL.get();
    }

    public static long getMemberId(){
        try {
            return MEMBER_LOGIN_RESP_THREAD_LOCAL.get().getId();
        }catch (Throwable e){
            log.info("获取会员信息异常 " + e);
            throw e;
        }
    }
}
