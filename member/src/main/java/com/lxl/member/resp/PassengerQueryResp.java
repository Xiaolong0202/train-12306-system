package com.lxl.member.resp;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/23  20:34
 **/
public class PassengerQueryResp {

    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 乘车人姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 旅客类型|枚举
     */
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
