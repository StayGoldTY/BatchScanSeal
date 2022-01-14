package com.kinggrid.scan.seal.servlet;

import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * 初始化log4j
 * @author xionglei
 * @version 1.0.0
 * 2021年09月10日 16:07
 */
public class Log4jInit extends HttpServlet {
    @Override
    public void init() throws ServletException {
        // 从web.xml配置读取，名字一定要和web.xml配置一致
        String prefix = getServletContext().getRealPath("/");
        String file = getInitParameter("log4j");
        if (file != null) {
            PropertyConfigurator.configure(prefix + file);
        }
    }
}
