package com.kinggrid.scan.seal.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties工具类
 *
 * @author xionglei
 * @version 1.0.0
 * 2021年09月10日 15:06
 */
public class PropertiesUtil {

    private static Properties properties = null;

    static {
        properties = PropertiesUtil.instance("../application.properties");
    }

    public static Properties getProperties() {
        return properties;
    }

    /**
     * 通过类路径获取Properties
     * @param classPath
     * @return
     */
    public static Properties instance(String classPath) {
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(classPath);
        // 使用properties对象加载输入流
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("读取properties：" + classPath + "异常！" + e.getMessage());
        }
        return properties;
    }

}
