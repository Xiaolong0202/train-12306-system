package com.lxl.batch.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class CronJobResp {
    private String group;

    private String name;

    private String description;

    private String state;

    private String cronExpression;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date nextFireTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date preFireTime;
}