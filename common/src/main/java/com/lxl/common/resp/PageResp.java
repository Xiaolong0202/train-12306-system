package com.lxl.common.resp;

import lombok.Data;

import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/23  21:54
 **/
@Data
public class PageResp<T> {
    private Long total;
    private List<T> list;
}
