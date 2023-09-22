package com.lxl.member.enums;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  15:14
 **/
public enum PassengerTypeEnum {

    ADULT("1","成人"),
    CHILD("2","小孩"),
    STUDENT("3","学生");

    PassengerTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private String code;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
