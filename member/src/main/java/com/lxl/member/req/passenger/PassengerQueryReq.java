package com.lxl.member.req.passenger;

import com.lxl.common.req.PageReq;
import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/23  20:27
 **/
@Data
public class PassengerQueryReq extends PageReq {
    private Long memberId;
}
