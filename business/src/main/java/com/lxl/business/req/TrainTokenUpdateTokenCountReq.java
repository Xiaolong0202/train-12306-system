package com.lxl.business.req;

import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/13  22:49
 **/
@Data
public class TrainTokenUpdateTokenCountReq {

    private Long id;
    private Integer tokenCount;
}
