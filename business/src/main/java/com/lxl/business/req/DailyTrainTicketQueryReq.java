package com.lxl.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxl.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/3  16:00
 **/
@Data
public class DailyTrainTicketQueryReq extends PageReq {
    private Long dailyTrainId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    private String start;

    private String end;
}
