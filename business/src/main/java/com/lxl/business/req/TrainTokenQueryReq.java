package com.lxl.business.req;


import com.lxl.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/13  22:29
 **/
@Data
public class TrainTokenQueryReq extends PageReq {
    private Long DailyTrainId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
}
