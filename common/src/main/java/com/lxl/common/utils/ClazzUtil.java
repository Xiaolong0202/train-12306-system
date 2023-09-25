package com.lxl.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/25  14:26
 **/
public class ClazzUtil {


    //通过一个枚举类的全限定名,获取一个枚举的所有枚举常量以及属性值
    public static List<Map<String,Object>> clazzEnumFields(String fullClazzName){
        List<Map<String,Object>> list = new ArrayList<>();
        Class<?> enumClass = null;
        try {
            enumClass = Class.forName(fullClazzName);
        if (enumClass.isEnum()){
            Object[] enumConstants = enumClass.getEnumConstants();
            for (Object enumConstant : enumConstants) {
                Map<String,Object> map = new HashMap<>();
                Class<?> clazz = enumConstant.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    // 忽略编译器生成的合成字段和特殊字段还有枚举常量字段,排除$VALUES（该枚举类型的所有枚举常量的数组）
                    if (!field.isEnumConstant()&&!field.isSynthetic() && !field.getName().contains("$VALUES")){
                        String fieldName = field.getName();
                        Object values = field.get(enumConstant);
                        map.put(fieldName,values);
                    }
                }
                list.add(map);
            }
        }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
