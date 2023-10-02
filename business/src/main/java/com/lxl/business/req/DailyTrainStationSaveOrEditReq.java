package com.lxl.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/26  20:55
 **/
@Data
public class DailyTrainStationSaveOrEditReq {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 车次编号
     */
    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dailyTrainId;



    /**
     * 车站序号
     */
    @NotNull
    private Integer trainIndex;

    /**
     * 站名
     */
    @NotBlank
    private String stationName;

    /**
     * 站名拼音
     */
    @NotBlank
    private String namePinyin;

    /**
     * 进站时间
     */
    @NotNull
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    private Date inTime;

    /**
     * 出站时间
     */
    @NotNull
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
    @NotNull
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
