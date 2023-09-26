package com.lxl.business.enums;

import java.math.BigDecimal;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/26  15:57
 **/
public enum TrainTypeEnum {

    G("G","高铁",new BigDecimal("1.2")),
    D("D","动车",new BigDecimal(1)),
    K("K","快速",new BigDecimal("0.8"));


    private String code;
    private String description;
    //票价比例
    private BigDecimal priceRate;

    TrainTypeEnum(String code, String description, BigDecimal priceRate) {
        this.code = code;
        this.description = description;
        this.priceRate = priceRate;
    }
}
