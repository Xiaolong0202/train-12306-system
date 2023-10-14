package com.lxl.common.constant;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/14  18:59
 **/
public interface RedisKeyPrefix {


    //会员令牌数
    public static final String TRAIN_TOKEN_COUNT = "TRAIN_TOKEN_COUNT";

    //防止会员频繁购买
    public static final String TRAIN_TOKEN_LOCK = "TRAIN_TOKEN_LOCK";

    public static final String DISTRIBUTE_LOCK = "DISTRIBUTE_LOCK";

    public static final String CAPTCHA_TOKEN = "CAPTCHA_TOKEN";
}
