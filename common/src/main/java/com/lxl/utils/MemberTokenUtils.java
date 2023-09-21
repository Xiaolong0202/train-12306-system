package com.lxl.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/21  17:40
 **/
public class MemberTokenUtils {

    private final static  String KEY = "1234";
    private final static String EXPIRE_TIME_KEY = "expire_time";
    private final static String MOBILE_KEY = "mobile";

    public static String generateToken(String mobile,long tokenLife){
        Map<String, Object> map = new HashMap<>() {
            {
                put(MOBILE_KEY, mobile);
                put(EXPIRE_TIME_KEY, System.currentTimeMillis() + tokenLife);
            }
        };
        return  JWTUtil.createToken(map, KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean verify(String token){
        JWT jwt = JWTUtil.parseToken(token);
        long payload = Long.parseLong(String.valueOf(jwt.getPayload(EXPIRE_TIME_KEY)));
        return JWTUtil.verify(token,KEY.getBytes(StandardCharsets.UTF_8))&&payload>System.currentTimeMillis();
    }

}
