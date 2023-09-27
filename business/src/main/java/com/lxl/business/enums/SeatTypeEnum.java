package com.lxl.business.enums;

import java.math.BigDecimal;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/27  14:36
 **/
public enum SeatTypeEnum {



    YDZ("1","一等座",new BigDecimal("0.4")),
    EDZ("2","二等座",new BigDecimal("0.3")),
    YW("3","硬卧",new BigDecimal("0.6")),
    RW("4","软卧",new BigDecimal("0.5"));

    private String code;
    private String description;

    /**
     * 用于票价计算，表示每公里的价格
     */
    private BigDecimal bigDecimal;

    SeatTypeEnum(String code, String description, BigDecimal bigDecimal) {
        this.code = code;
        this.description = description;
        this.bigDecimal = bigDecimal;
    }


}
