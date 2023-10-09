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
    PASSENGER_ALREADY_EXIST("该乘客已经存在"),
    TRAIN_ALREADY_EXIST("该车次已经存在"),
    STATION_ALREADY_EXIST("该车站已经存在"),
    TRAIN_CARRIAGE_ALREADY_EXIST("该车厢已经存在"),
    TRAIN_STATION_STATION_NAME_ALREADY_EXIST("同车次站名已经存在"),
    TRAIN_STATION_INDEX_ALREADY_EXIST("同车次站序已经存在"),
    NO_DAILY_TRAIN_TICKET_INFO("没有对应的车次信息"),
    TICKET_INSUFFICIENT_ERROR("余票不足"),
    WRONG_ENUM_CODE("错误的枚举CODE"),
    CUSTOM_ERROR("CUSTOM_ERROR"),
    SERVER_BUSY("服务器正忙,请稍后重试");

    public BussinessExceptionEnum setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public  String desc ;

    BussinessExceptionEnum(String desc) {
        this.desc = desc;
    }


}
