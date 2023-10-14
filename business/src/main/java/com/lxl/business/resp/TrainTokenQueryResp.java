package com.lxl.business.resp;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/13  22:33
 **/
@Data
public class TrainTokenQueryResp {
    /**
     * ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 每日车次id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dailyTrainId;

    /**
     * 发车日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 列车编号
     */
    private String trainCode;

    /**
     * 车次类型|枚举
     */
    private String type;


    /**
     * 令牌余量
     */
    private Integer tokenCount;
}
