package com.lxl.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/27  16:11
 **/
@Data
public class TrainSeatSaveOrEditReq {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 火车编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long trainId;

    /**
     * 火车箱号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long carriageId;

    /**
     * 座位类型|枚举
     */
    private String seatType;

    /**
     * 排
     */
    @Pattern(regexp = "[0-9]{1,2}",message = "请输入一个两位数")
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
