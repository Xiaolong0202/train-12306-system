package com.lxl.business.req;

import com.lxl.common.req.PageReq;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/26  15:52
 **/
@Data
public class TrainStationQueryReq extends PageReq implements Serializable{
    private Long trainId;
}
