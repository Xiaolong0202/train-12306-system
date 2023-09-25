package com.lxl.business.req;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/22  15:40
 **/
@Data
public class StationSaveOrEditReq {

    /**
     * id
     */
    private Long id;

    /**
     * 站名
     */
    private String name;

    /**
     * 站名拼音
     */
    private String namePinyin;

    /**
     * 站名拼音首字母
     */
    private String namePy;

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
