package com.lxl.business.mapper;

import com.lxl.business.domain.DailyTrainTicket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13430
* @description 针对表【daily_train_ticket(余票信息)】的数据库操作Mapper
* @createDate 2023-10-03 15:58:01
* @Entity com.lxl.business.domain.DailyTrainTicket
*/
@Mapper
public interface DailyTrainTicketMapper extends BaseMapper<DailyTrainTicket> {

}




