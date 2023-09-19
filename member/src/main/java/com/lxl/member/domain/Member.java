package com.lxl.member.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  21:19
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Member {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String mobile;
}
