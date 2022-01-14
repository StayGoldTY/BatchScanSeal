package com.kinggrid.scan.seal.listener;

import com.kinggrid.scan.seal.service.impl.BatchStampSealServiceImpl;
import com.kinggrid.scan.seal.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.Properties;

/**
 * 监听器
 * @author xionglei
 * @version 1.0.0
 * 2021年09月11日 17:10
 */
public class InitWebContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // 设置web工程的绝对路径,设置日志路径
        String webappPath = event.getServletContext().getRealPath("");
        System.setProperty("user.dir", webappPath);

        Properties properties = PropertiesUtil.getProperties();
        String pdfPath = properties.getProperty("pdf.path");
        if (StringUtils.isNotBlank(pdfPath)) {
            pdfPath = pdfPath.replace("${user.dir}", webappPath);
            File file = new File(pdfPath + BatchStampSealServiceImpl.stampPdfDir);
            // 初始化盖章文件目录
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
