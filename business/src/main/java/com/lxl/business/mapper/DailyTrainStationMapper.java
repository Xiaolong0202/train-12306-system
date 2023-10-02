package com.lxl.business.mapper;

import com.lxl.business.domain.DailyTrainStation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 13430
* @description 针对表【daily_train_station(每日车站)】的数据库操作Mapper
* @createDate 2023-10-02 09:53:18
* @Entity com.lxl.business.domain.DailyTrainStation
*/
@Mapper
public interface DailyTrainStationMapper extends BaseMapper<DailyTrainStation> {

    void insertBatch(@Param("list") List<DailyTrainStation> dailyTrainStations);
}




