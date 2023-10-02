package com.lxl.business.req;

import com.lxl.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/26  15:52
 **/
@Data
public class DailyTrainStationQueryReq extends PageReq {

    private Long dailyTrainId;

}
