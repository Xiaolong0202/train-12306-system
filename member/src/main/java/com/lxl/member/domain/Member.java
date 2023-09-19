package com.lxl.member.domain;

import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  21:19
 **/

public class Member {

    @TableId
    private long id;

    private String mobile;
}
