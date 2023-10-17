package com.lxl.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 每日车站
 * @author 13430
 * @TableName daily_train_station
 */
@TableName(value ="daily_train_station")
@Data
public class DailyTrainStation implements Serializable {
    /**
     * id
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 车次id
     */
    private Long dailyTrainId;


    /**
     * 车站序号
     */
    private Integer trainIndex;

    /**
     * 站名
     */
    private String stationName;

    /**
     * 站名拼音
     */
    private String namePinyin;

    /**
     * 进站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date inTime;

    /**
     * 出站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date outTime;

    /**
     * 停站时间
     */
    @JsonFormat(pattern = "mm:ss",timezone = "GMT+8")
    private Date stopTime;

    /**
     * 里程（公里）| 从上一站到本站的距离
     */
    private BigDecimal km;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}