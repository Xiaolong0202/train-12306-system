package com.lxl.interceptor;

import cn.hutool.core.util.StrUtil;
import com.lxl.context.MemberInfoContext;
import com.lxl.resp.MemberLoginResp;
import com.lxl.utils.MemberTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  23:40
 **/
@Slf4j
@Component
public class MemberInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)){
            log.info("获取当前会员登录token = " + token);
            String mobile = String.valueOf(MemberTokenUtils.getAttr(token,MemberTokenUtils.MOBILE_KEY));
            log.info("当前登录会员为： " + mobile);
            long memberId = Long.parseLong(String.valueOf(MemberTokenUtils.getAttr(token,MemberTokenUtils.MEMBER_ID_KEY)));
            log.info("当前会员ID为： "+memberId);
            MemberInfoContext.set(new MemberLoginResp(memberId,mobile,token));
        }
        return true;
    }
}
