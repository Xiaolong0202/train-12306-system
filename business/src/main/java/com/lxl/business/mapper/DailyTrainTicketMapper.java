package com.lxl.business.mapper;

import com.lxl.business.domain.DailyTrainTicket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 13430
* @description 针对表【daily_train_ticket(余票信息)】的数据库操作Mapper
* @createDate 2023-10-03 15:58:01
* @Entity com.lxl.business.domain.DailyTrainTicket
*/
@Mapper
public interface DailyTrainTicketMapper extends BaseMapper<DailyTrainTicket> {

    void updateBySell(@Param("daily_train_id") Long dailyTrainId, @Param("seatType") String seatType, @Param("minStart") Integer minStart, @Param("maxStart") Integer maxStart, @Param("minEnd") Integer minEnd, @Param("maxEnd") Integer maxEnd);
}




