package com.lxl.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 每日车站
 * @TableName daily_train_station
 */
@TableName(value ="daily_train_station")
@Data
public class DailyTrainStation implements Serializable {
    /**
     * id
     */
    @TableId
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
    private Date inTime;

    /**
     * 出站时间
     */
    private Date outTime;

    /**
     * 停站时间
     */
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