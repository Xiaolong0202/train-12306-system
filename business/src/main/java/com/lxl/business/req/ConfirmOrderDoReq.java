package com.lxl.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/5  12:43
 **/
@Data
public class ConfirmOrderDoReq {


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
    @NotNull
    private List<ConfirmOrderTicketReq> tickets;


}
