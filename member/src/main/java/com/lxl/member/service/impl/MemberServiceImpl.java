package com.lxl.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxl.exception.BusinessException;
import com.lxl.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.member.domain.Member;
import com.lxl.member.mapper.MemberMapper;
import com.lxl.member.service.MemberService;
import com.lxl.utils.SnowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  20:41
 **/
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;


    @Override
    public int count() {
        return memberMapper.count();
    }

    @Override
    public long register( String mobile) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getMobile,mobile);
        List<Member> members = memberMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(members)){
            throw new BusinessException(BussinessExceptionEnum.MEMBER_REGISTER_ERROR);
        }
        Member entity = new Member();
        entity.setId(SnowUtils.nextSnowId());
        entity.setMobile(mobile);
        memberMapper.insert(entity);
        return entity.getId();
    }
}
