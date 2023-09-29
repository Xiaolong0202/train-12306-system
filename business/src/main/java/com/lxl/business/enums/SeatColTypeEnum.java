package com.lxl.business.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/27  15:39
 **/
public enum SeatColTypeEnum {
    YDZ_A("YDZ_A","A","1"),
    YDZ_C("YDZ_C","C","1"),
    YDZ_D("YDZ_D","D","1"),
    YDZ_F("YDZ_F","F","1"),

    EDZ_A("EDZ_A","A","2"),
    EDZ_B("EDZ_B","B","2"),
    EDZ_C("EDZ_C","C","2"),
    EDZ_D("EDZ_D","D","2"),
    EDZ_F("EDZ_F","F","2"),

    YW_A("YW_A","下铺左","3"),
    YW_B("YW_B","下铺右","3"),
    YW_C("YW_C","中铺左","3"),
    YW_D("YW_D","中铺右","3"),
    YW_E("YW_E","上铺左","3"),
    YW_F("YW_F","上铺右","3"),

    RW_A("RW_A","下铺左","4"),
    RW_B("RW_B","下铺右","4"),
    RW_C("RW_C","上铺左","4"),
    RW_D("RW_D","上铺右","4"),


    ;

    public String code;
    public String description;
    //关联seatType Code
    public String type;

    SeatColTypeEnum(String code, String description, String type) {
        this.code = code;
        this.description = description;
        this.type = type;
    }

    public static List<SeatColTypeEnum> getSeatCols(String type){
        List<SeatColTypeEnum> list = new ArrayList<>();
        EnumSet<SeatColTypeEnum> seatColTypeEnums = EnumSet.allOf(SeatColTypeEnum.class);
        for (SeatColTypeEnum seatColTypeEnum : seatColTypeEnums) {
            if (type.equals(seatColTypeEnum.type)){
                list.add(seatColTypeEnum);
            }
        }
        return list;
    }
}
