package com.lxl.resp;

import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  22:26
 **/
@Data
public class CommonResp<T>{
    private boolean success;
    private String message;
    private T content;

    public CommonResp(T content) {
        this.content = content;
        this.success = true;
        this.message = "请求成功";
    }
}
