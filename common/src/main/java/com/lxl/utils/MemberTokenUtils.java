package com.lxl.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/21  17:40
 **/
@Slf4j
public class MemberTokenUtils {

    private final static  String KEY = "1234";
    public final static String EXPIRE_TIME_KEY = "expire_time";
    public final static String MOBILE_KEY = "mobile";
    public final static String MEMBER_ID_KEY = "memberId";

    public static String generateToken(String mobile,long memberId,long tokenLife){
        Map<String, Object> map = new HashMap<>() {
            {
                put(MOBILE_KEY, mobile);
                put(MEMBER_ID_KEY,memberId);
                put(EXPIRE_TIME_KEY, System.currentTimeMillis() + tokenLife);
            }
        };
        return  JWTUtil.createToken(map, KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean verify(String token){
        try {
            JWT jwt = JWTUtil.parseToken(token);
            long payload = Long.parseLong(String.valueOf(jwt.getPayload(EXPIRE_TIME_KEY)));
            return JWTUtil.verify(token,KEY.getBytes(StandardCharsets.UTF_8))&&payload>System.currentTimeMillis();
        }catch (Throwable e){
            log.info("解析token时出现异常 " + e);
            return false;
        }
    }

    public static Object getAttr(String token,String attrKey){
        try {
            JWT jwt = JWTUtil.parseToken(token);
            Object payload = jwt.getPayload(attrKey);
            return payload;
        }catch (Throwable e){
            log.info("解析token时出现异常 " + e);
            return null;
        }
    }

}
