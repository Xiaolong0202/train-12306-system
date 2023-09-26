package com.lxl.business.mapper;

import com.lxl.business.domain.Train;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author 13430
* @description 针对表【train(车次)】的数据库操作Mapper
* @createDate 2023-09-26 15:50:20
* @Entity com.lxl.business.domain.Train
*/
@Mapper
public interface TrainMapper extends BaseMapper<Train> {

    @Select("select `code` from train_business.train where id = #{id}")
    String selectCodeById(Long id);

}




