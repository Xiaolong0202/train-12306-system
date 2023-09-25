package com.lxl.business.mapper;

import com.lxl.business.domain.Station;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author 13430
* @description 针对表【station】的数据库操作Mapper
* @createDate 2023-09-25 21:24:35
* @Entity com.lxl.business.domain.Station
*/
@Mapper
public interface StationMapper extends BaseMapper<Station> {

    @Select("select `name` from train_business.station where id = #{id}")
    String selectNameById(Long id);
}




