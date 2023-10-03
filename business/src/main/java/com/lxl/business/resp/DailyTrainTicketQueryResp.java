package com.lxl.business.resp;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/3  16:01
 **/
@Data
public class DailyTrainTicketQueryResp {

    /**
     * id
     */
   @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 每日车次id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dailyTrainId;

    /**
     * 出发站
     */
    private String start;

    /**
     * 出发站拼音
     */
    private String startPinyin;

    /**
     * 出发时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

    /**
     * 出发站序|本站是整个车次的第几站
     */
    private Integer startIndex;

    /**
     * 到达站
     */
    private String end;

    /**
     * 到达站拼音
     */
    private String endPinyin;

    /**
     * 到站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

    /**
     * 到站站序|本站是整个车次的第几站
     */
    private Integer endIndex;

    /**
     * 一等座余票
     */
    private Integer ydz;

    /**
     * 一等座票价
     */
    private BigDecimal ydzPrice;

    /**
     * 二等座余票
     */
    private Integer edz;

    /**
     * 二等座票价
     */
    private BigDecimal edzPrice;

    /**
     * 软卧余票
     */
    private Integer rw;

    /**
     * 软卧票价
     */
    private BigDecimal rwPrice;

    /**
     * 硬卧余票
     */
    private Integer yw;

    /**
     * 硬卧票价
     */
    private BigDecimal ywPrice;

    /**
     * 新增时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
