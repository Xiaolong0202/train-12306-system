package com.lxl.business.enums;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/27  15:39
 **/
public enum SeatColEnum {
    YDZ_A("A","A","1"),
    YDZ_C("C","C","1"),
    YDZ_D("D","D","1"),
    YDZ_F("F","F","1"),

    EDZ_A("A","A","2"),
    EDZ_B("B","B","2"),
    EDZ_C("C","C","2"),
    EDZ_D("D","D","2"),
    EDZ_F("F","F","2");

    private String code;
    private String description;
    //关联seatType Code
    private String type;

    SeatColEnum(String code, String description, String type) {
        this.code = code;
        this.description = description;
        this.type = type;
    }
}
