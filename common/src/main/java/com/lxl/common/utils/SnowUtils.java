package com.lxl.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;


/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/20  14:24
 **/
public class SnowUtils {

    private static  long DATA_CENTER_ID = 1;
    private static  long MACHINE_ID = 1;

    private static  Snowflake SNOWFLAKE = IdUtil.getSnowflake(DATA_CENTER_ID,MACHINE_ID);

    public static long nextSnowId(){
       return SNOWFLAKE.nextId();
    }

    public static String nextSnowIdStr(){
        return String.valueOf(SNOWFLAKE.nextId());
    }


    public static void updateSnowflake(){
        SNOWFLAKE =  IdUtil.getSnowflake(DATA_CENTER_ID,MACHINE_ID);
    }

    public static long getDataCenterId() {
        return DATA_CENTER_ID;
    }

    public static void setDataCenterId(long dataCenterId) {
        DATA_CENTER_ID = dataCenterId;
    }

    public static long getMachineId() {
        return MACHINE_ID;
    }

    public static void setMachineId(long machineId) {
        MACHINE_ID = machineId;
    }


}
