package com.lxl.generator.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/24  23:48
 **/
public class FreeMarkUtil {

    static String FTL_PATH = "generator/src/main/java/com/lxl/generator/ftl";//存放ftl文件的文件夹路径

    static Template template;

    public static void initialConfig(String fileName) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);

        configuration.setDirectoryForTemplateLoading(new File(FTL_PATH));
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
        template = configuration.getTemplate(fileName);
    }

    public static void generator(String fileName, Map<String,Object> map) throws IOException, TemplateException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        template.process(map,bufferedWriter);
        bufferedWriter.flush();
    }

    public static void main(String[] args) throws IOException {
        initialConfig("test.ftl");
    }
}
