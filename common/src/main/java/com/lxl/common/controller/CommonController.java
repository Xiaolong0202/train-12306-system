package com.lxl.common.controller;

import com.lxl.common.utils.ClazzUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/25  14:42
 **/
@RestController
public class CommonController {


    @GetMapping("/get-enum/{prefix}")
    public List<Map<String,Object>> getEnum(@PathVariable("prefix") String prefix){
        return ClazzUtil.clazzEnumFields("com.lxl."+prefix+"TypeEnum");
    }


}
