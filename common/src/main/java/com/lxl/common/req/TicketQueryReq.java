package com.lxl.common.req;

import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/23  20:27
 **/
@Data
public class TicketQueryReq extends PageReq {
    private Long memberId;
}
