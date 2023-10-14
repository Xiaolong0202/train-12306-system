package com.lxl.business.mapper;

import com.lxl.business.domain.TrainToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
* @author 13430
* @description 针对表【train_token(秒杀令牌)】的数据库操作Mapper
* @createDate 2023-10-13 23:25:40
* @Entity com.lxl.business.domain.TrainToken
*/
@Mapper
public interface TrainTokenMapper extends BaseMapper<TrainToken> {


    int decreaseToken(@Param("date") Date date, @Param("trainCode") String trainCode);
}




