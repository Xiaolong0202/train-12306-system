package com.lxl.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;


/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/20  14:24
 **/
public class SnowUtils {

    private static final long DATA_CENTER_ID = 1;
    private static final long MACHINE_ID = 1;

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(DATA_CENTER_ID,MACHINE_ID);

    public static long nextSnowId(){
       return SNOWFLAKE.nextId();
    }

    public static String nextSnowIdStr(){
        return String.valueOf(SNOWFLAKE.nextId());
    }




}
