package com.lxl.member.service;

import com.lxl.member.domain.Member;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  20:41
 **/
public interface MemberService {

    int count();

    long register(Member member);

}
