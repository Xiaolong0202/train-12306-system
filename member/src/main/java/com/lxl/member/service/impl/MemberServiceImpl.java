package com.lxl.member.service.impl;

import com.lxl.member.mapper.MemberMapper;
import com.lxl.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  20:41
 **/
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;


    @Override
    public int count() {
        return memberMapper.count();
    }
}
