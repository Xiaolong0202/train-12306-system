package com.lxl.business.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/16  15:59
 **/
@Data
public class ConfirmOrderMQDTO {
   private Date date;
   private String trainCode;
}
