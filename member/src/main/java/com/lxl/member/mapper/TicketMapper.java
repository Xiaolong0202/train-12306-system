package com.lxl.member.mapper;

import com.lxl.member.domain.Ticket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13430
* @description 针对表【ticket(车票)】的数据库操作Mapper
* @createDate 2023-10-07 14:40:16
* @Entity com.lxl.member.domain.Ticket
*/
@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {

}




