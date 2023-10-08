package com.lxl.common.req;

import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/23  21:46
 **/
@Data
@EqualsAndHashCode
public class PageReq implements Serializable {

    public Integer currentPage;

    @Max(value = 100,message = "页面大小最大不能超过100")
    public Integer pageSize;
}
