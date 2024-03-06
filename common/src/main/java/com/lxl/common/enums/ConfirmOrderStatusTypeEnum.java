package com.lxl.common.enums;

public enum ConfirmOrderStatusTypeEnum {

    INIT("I", "初始"),
    PENDING("P", "处理中"),
    SUCCESS("S", "选座成功"),
    PAY_SUCCESS("PS","支付成功"),
    PAY_PENDING("PP","支付中"),
    FAILURE("F", "失败"),
    EMPTY("E", "无票"),
    CANCEL("C", "取消");

    private String code;

    private String desc;

    ConfirmOrderStatusTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override    public String toString() {
        return "ConfirmOrderStatusEnum{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                "} " + super.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
