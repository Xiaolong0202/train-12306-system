package com.lxl.business.req;

import com.lxl.common.req.PageReq;
import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/27  16:09
 **/
@Data
public class DailyTrainSeatQueryReq extends PageReq {
    private Long dailyTrainId;
}
