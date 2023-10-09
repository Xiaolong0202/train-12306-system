package com.lxl.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 每日座位
 * @TableName daily_train_seat
 */
@TableName(value ="daily_train_seat")
@Data
public class DailyTrainSeat implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 每日车次id
     */
    private Long dailyTrainId;

    /**
     * 火车箱号
     */
    private Long carriageId;

    /**
     * 所在车厢
     */
    private Integer carriageIndex;

    /**
     * 座位类型|枚举
     */
    private String seatType;

    /**
     * 排
     */
    private String seatRow;

    /**
     * 列|枚举
     */
    private String seatCol;

    /**
     * 同车厢座序
     */
    private Integer carriageSeatIndex;

    /**
     * 售卖情况|比如100100就是表示第一站与第四站被卖出去了
     */
    private String seatSell;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}