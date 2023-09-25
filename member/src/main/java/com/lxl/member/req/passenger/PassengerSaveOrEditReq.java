package com.lxl.member.req.passenger;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",message = "身份证号格式不对")
    private String idCard;

    /**
     * 旅客类型|枚举
     */
    @NotBlank(message = "乘车人类型不能为空")
    @Pattern(regexp = "[123]?",message = "乘客类型参数异常")
    private String type;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
