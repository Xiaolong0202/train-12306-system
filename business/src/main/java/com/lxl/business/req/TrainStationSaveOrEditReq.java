package com.lxl.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/26  20:55
 **/
@Data
public class TrainStationSaveOrEditReq {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 车次编号
     */
    private String trainCode;

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
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date inTime;

    /**
     * 出站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date outTime;

    /**
     * 停站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date stopTime;

    /**
     * 里程（公里）| 从上一站到本站的距离
     */
    private BigDecimal km;

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
