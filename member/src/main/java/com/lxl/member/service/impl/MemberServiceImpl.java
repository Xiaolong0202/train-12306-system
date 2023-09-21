package com.lxl.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxl.exception.BusinessException;
import com.lxl.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.member.domain.Member;
import com.lxl.member.mapper.MemberMapper;
import com.lxl.member.req.MemberLoginReq;
import com.lxl.member.resp.MemberLoginResp;
import com.lxl.member.service.MemberService;
import com.lxl.utils.MemberTokenUtils;
import com.lxl.utils.SnowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    public long register(String mobile) {
        Member member = getMember(mobile);
        if (!ObjectUtils.isEmpty(member)) {
            throw new BusinessException(BussinessExceptionEnum.MEMBER_REGISTER_ERROR);
        }
        Member entity = new Member();
        entity.setId(SnowUtils.nextSnowId());
        entity.setMobile(mobile);
        memberMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void sendCode(String mobile) {
        Member member = getMember(mobile);
        if (ObjectUtils.isEmpty(member)) {
            log.info("数据库中没有手机号,插入");
            Member entity = new Member();
            entity.setId(SnowUtils.nextSnowId());
            entity.setMobile(mobile);
            memberMapper.insert(entity);
        } else {
            log.info("该手机号已经存在");
        }
        String code = RandomUtil.randomString(4);
        log.info("验证码{}", code);
    }

    @Override
    public MemberLoginResp login(MemberLoginReq req) {
        Member member = getMember(req.getMobile());
        if (ObjectUtils.isEmpty(member)){
            throw new BusinessException(BussinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }
        if (!"8888".equals(req.getCode())){
            throw new BusinessException(BussinessExceptionEnum.MEMBER_CODE_ERROR);
        }
        MemberLoginResp memberLoginResp = new MemberLoginResp();
        BeanUtils.copyProperties(member,memberLoginResp);
        memberLoginResp.setToken(MemberTokenUtils.generateToken(member.getMobile(),1000*60*10));//生命周期为10分钟
        return memberLoginResp;
    }

    private Member getMember(String mobile) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getMobile, mobile);
        List<Member> members = memberMapper.selectList(queryWrapper);
        return CollUtil.isEmpty(members) ? null : members.get(0);
    }
}
