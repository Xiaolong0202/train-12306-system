package com.lxl.exception;

import com.lxl.exception.exceptionEnum.BussinessExceptionEnum;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  23:16
 **/
public class BusinessException extends RuntimeException{

    private final BussinessExceptionEnum exceptionEnum;

    public BussinessExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public BusinessException(BussinessExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}
