package com.lxl.member.mapper;

import com.lxl.member.domain.Passenger;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13430
* @description 针对表【passenger(乘客)】的数据库操作Mapper
* @createDate 2023-09-22 15:11:55
* @Entity com.lxl.member.domain.Passenger
*/
@Mapper
public interface PassengerMapper extends BaseMapper<Passenger> {

    String selectIdCardByIdInt(Long id);
}




