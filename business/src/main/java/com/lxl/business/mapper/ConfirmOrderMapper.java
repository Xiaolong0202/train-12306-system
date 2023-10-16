package com.lxl.business.mapper;

import com.lxl.business.domain.ConfirmOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 13430
* @description 针对表【confirm_order(确认订单)】的数据库操作Mapper
* @createDate 2023-10-05 11:47:00
* @Entity com.lxl.business.domain.ConfirmOrder
*/
@Mapper
public interface ConfirmOrderMapper extends BaseMapper<ConfirmOrder> {
    int queryOrderQueueStatus(@Param("confirmOrder") ConfirmOrder confirmOrder, @Param("initCode") String initCode, @Param("pendingCode") String pendingCode);
}




