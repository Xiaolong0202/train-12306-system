package com.lxl.business.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/27  16:07
 **/
@Data
public class DailyTrainSeatQueryResp {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 火车编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dailyTrainId;

    /**
     * 火车箱号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carriageId;



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
     * 列
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
