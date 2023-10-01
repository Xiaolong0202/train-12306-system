package com.lxl.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/26  15:52
 **/
@Data
public class DailyTrainSaveOrEditReq {
    private Long id;


    /**
     * 日期
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startDate;

    /**
     * 车次编号
     */
    @NotBlank
    private String code;

    /**
     * 车次类型|枚举
     */
    @NotBlank
    private String type;

    /**
     * 始发站
     */
    @NotBlank
    private String start;

    /**
     * 始发站拼音
     */
    @NotBlank
    private String startPinyin;

    /**
     * 出发时间
     */
    @NotNull
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

    /**
     * 终点站
     */
    @NotBlank
    private String end;

    /**
     * 终点站拼音
     */
    @NotBlank
    private String endPinyin;

    /**
     * 到终点站时间
     */
    @NotNull
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

    /**
     * 出发和到站之间间隔的天数
     */
    @NotNull
    private Integer intervalDay;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
