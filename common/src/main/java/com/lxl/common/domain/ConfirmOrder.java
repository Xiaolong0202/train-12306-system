package com.lxl.common.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 确认订单
 * @TableName confirm_order
 */
@TableName(value ="confirm_order")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOrder implements Serializable {
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
     * 日期
     */
    private Date date;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 出发站
     */
    private String start;

    /**
     * 到达站
     */
    private String end;

    /**
     * 余票ID
     */
    private Long dailyTrainTicketId;

    /**
     * 车票
     */
    private String tickets;

    /**
     * 订单状态|枚举[ConfirmOrderStatusEnum]
     */
    private String status;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    private BigDecimal sumPrice;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}