package com.lxl.generator.server;

import com.lxl.generator.util.FreeMarkUtil;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/25  0:02
 **/
public class ServerGenerator {

    static String toPath = "generator/src/main/java/com/lxl/generator/aimDir";

    static {
        new File(toPath).mkdirs();
    }

    public static void main(String[] args) throws IOException, TemplateException {
        FreeMarkUtil.initialConfig("test.ftl");
        FreeMarkUtil.generator(toPath+'/'+"Test.java",Map.of("domain","Test"));
    }
}
