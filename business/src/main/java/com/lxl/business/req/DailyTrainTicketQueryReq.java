package com.lxl.business.req;

import com.lxl.common.req.PageReq;
import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/3  16:00
 **/
@Data
public class DailyTrainTicketQueryReq extends PageReq {
    private Long dailyTrainId;
}
