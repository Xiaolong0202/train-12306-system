package com.lxl.member.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/20  13:49
 **/
@Data
public class MemberRegisterReq {

    @NotBlank(message = "手机号不能为空")
    private String mobile;
}
