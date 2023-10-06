package com.lxl.business.resp;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lxl.business.req.ConfirmOrderTicketReq;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/5  11:48
 **/
@Data
public class ConfirmOrderQueryResp {

    /**
     * 会员id
     */
    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private Long memberId;

    /**
     * 日期
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date date;

    /**
     * 车次编号
     */
    @NotBlank
    private String trainCode;

    /**
     * 出发站
     */
    @NotBlank
    private String start;

    /**
     * 到达站
     */
    @NotBlank
    private String end;

    /**
     * 余票ID
     */
    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dailyTrainTicketId;

    /**
     * 车票
     */
    @NotNull(message = "车票不能为空")
    private String tickets;

    /**
     * 订单状态|枚举[ConfirmOrderStatusEnum]
     */
    private String status;

    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
