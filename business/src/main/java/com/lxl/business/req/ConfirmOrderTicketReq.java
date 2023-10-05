package com.lxl.business.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/10/5  12:44
 **/
@Data
public class ConfirmOrderTicketReq {
    @NotNull
    private Long passengerId;
    @NotBlank
    private String name;
    @NotBlank
    private String idCard;
    @NotBlank
    private String passengerType;
    @NotBlank
    private String seatType;
    @NotBlank
    private String seat;
}
