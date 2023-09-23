package com.lxl.req;

import jakarta.validation.constraints.Max;
import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/23  21:46
 **/

@Data
public class PageReq {

    private Integer currentPage;

    @Max(value = 100,message = "页面大小最大不能超过100")
    private Integer pageSize;
}
