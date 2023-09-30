package com.lxl.batch.req;

import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/30  16:22
 **/

@Data
public class CronJobReq {

    private String group;
    private String name;
    private String description;
    private String cronExpression;

}
