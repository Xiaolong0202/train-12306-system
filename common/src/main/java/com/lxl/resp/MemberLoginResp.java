package com.lxl.resp;

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

    Long id;

    String mobile;

    String token;

}
