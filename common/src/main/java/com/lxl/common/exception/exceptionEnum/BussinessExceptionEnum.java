package com.lxl.common.exception.exceptionEnum;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  23:17
 **/
public enum BussinessExceptionEnum {
    MEMBER_REGISTER_ERROR("该手机号已存在"),
    MEMBER_MOBILE_NOT_EXIST("请填写已经注册的手机号"),
    MEMBER_CODE_ERROR("验证码填写错误"),
    PASSENGER_ALREADY_EXIST("该乘客已经存在");

    public final String desc ;

    BussinessExceptionEnum(String desc) {
        this.desc = desc;
    }


}
