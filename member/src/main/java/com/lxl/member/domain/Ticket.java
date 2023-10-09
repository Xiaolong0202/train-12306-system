package com.lxl.member.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 车票
 * @TableName ticket
 */
@TableName(value ="ticket")
@Data
public class Ticket implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 乘客id
     */
    private Long passengerId;

    /**
     * 车次票的ID
     */
    private Long dailyTrainTicketId;

    /**
     * 乘客姓名
     */
    private String passengerName;

    /**
     * 日期
     */
    private Date trainDate;

    /**
     * 车次编号 包含了车次类型
     */
    private String trainCode;

    /**
     * 箱序
     */
    private Integer carriageIndex;

    /**
     * 排号|01, 02
     */
//    @TableField("`row`")
    private String seatRow;

    /**
     * 列号|枚举[SeatColEnum]
     */
//    @TableField("`col`")
    private String seatCol;

    /**
     * 出发站
     */
    private String startStation;

    /**
     * 出发时间
     */
    private Date startTime;

    /**
     * 到达站
     */
    private String endStation;

    /**
     * 到站时间
     */
    private Date endTime;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    private String seatType;

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