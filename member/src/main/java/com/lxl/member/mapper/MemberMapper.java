package com.lxl.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxl.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  20:35
 **/
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    int count();
}
