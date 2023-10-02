package com.lxl.business.mapper;

import com.lxl.business.domain.DailyTrainCarriage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 13430
* @description 针对表【daily_train_carriage(每日车厢)】的数据库操作Mapper
* @createDate 2023-10-02 11:22:38
* @Entity com.lxl.business.domain.DailyTrainCarriage
*/
@Mapper
public interface DailyTrainCarriageMapper extends BaseMapper<DailyTrainCarriage> {

    void insertBatch(@Param("list") List<DailyTrainCarriage> dailyTrainCarriages);
}




