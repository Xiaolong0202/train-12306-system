package com.lxl.member.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  15:40
 **/
@Data
public class PassengerSaveOrEditReq {

    private Long id;

    /**
     * 会员id
     */
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    /**
     * 乘车人姓名
     */
    @NotBlank(message = "乘车人姓名不能为空")
    private String name;

    /**
     * 身份证
     */
    @NotBlank(message = "身份证不能为空")
    private String idCard;

    /**
     * 旅客类型|枚举
     */
    @NotBlank(message = "乘车人类型不能为空")
    private String type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
