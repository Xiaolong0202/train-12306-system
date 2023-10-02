package com.lxl.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/27  14:42
 **/
@Data
public class DailyTrainCarriageSaveOrEditReq {

    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dailyTrainId;

    /**
     * 火车箱号
     */
    @NotNull
    private Integer trainIndex;

    /**
     * 座位类型|枚举
     */
    @NotNull
    private String seatType;

    /**
     * 排数
     */
    private Integer seatCount;

    /**
     * 排数
     */
    @Max(value = 99,message = "行数不要超过99")
    private Integer rowCount;

    /**
     * 列数
     */
    private Integer columnCount;

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
