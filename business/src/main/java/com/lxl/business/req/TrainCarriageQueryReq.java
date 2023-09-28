package com.lxl.business.req;

import com.lxl.common.req.PageReq;
import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/27  14:42
 **/
@Data
public class TrainCarriageQueryReq extends PageReq {
    private Long trainId;
}
