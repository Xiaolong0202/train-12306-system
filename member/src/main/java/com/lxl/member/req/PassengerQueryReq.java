package com.lxl.member.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxl.req.PageReq;
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
