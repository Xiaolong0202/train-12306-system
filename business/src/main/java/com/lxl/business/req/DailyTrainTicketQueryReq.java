package com.lxl.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lxl.common.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/3  16:00
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DailyTrainTicketQueryReq extends PageReq implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dailyTrainId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    private String start;

    private String end;
}
