package com.lxl.common.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/21  13:15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginResp {

    @JsonSerialize(using = ToStringSerializer.class)
    Long id;

    String mobile;

    String token;

}
