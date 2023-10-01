package com.lxl.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 每日车次
 * @TableName daily_train
 */
@TableName(value ="daily_train")
@Data
public class DailyTrain implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 日期
     */
    private Date startDate;

    /**
     * 车次编号
     */
    private String code;

    /**
     * 车次类型|枚举
     */
    private String type;

    /**
     * 始发站
     */
    private String start;

    /**
     * 始发站拼音
     */
    private String startPinyin;

    /**
     * 出发时间
     */
    private Date startTime;

    /**
     * 终点站
     */
    private String end;

    /**
     * 终点站拼音
     */
    private String endPinyin;

    /**
     * 到终点站时间
     */
    private Date endTime;

    /**
     * 出发和到站之间间隔的天数
     */
    private Integer intervalDay;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}