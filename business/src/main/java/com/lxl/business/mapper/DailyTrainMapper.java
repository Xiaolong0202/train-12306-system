package com.lxl.business.mapper;

import com.lxl.business.domain.DailyTrain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13430
* @description 针对表【daily_train(每日车次)】的数据库操作Mapper
* @createDate 2023-10-01 13:49:18
* @Entity com.lxl.business.domain.DailyTrain
*/
@Mapper
public interface DailyTrainMapper extends BaseMapper<DailyTrain> {

}




