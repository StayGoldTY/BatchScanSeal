package com.kinggrid.scan.seal.servlet;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.kinggrid.scan.seal.model.PageResult;
import com.kinggrid.scan.seal.model.PdfFile;
import com.kinggrid.scan.seal.service.impl.BatchStampSealServiceImpl;
import com.kinggrid.scan.seal.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 获取pdf文件
 * @author xionglei
 * @version 1.0.0
 * 2021年09月10日 19:20
 */
public class GetFileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties properties = PropertiesUtil.getProperties();
        String pdfPath = properties.getProperty("pdf.path");
        pdfPath = pdfPath.replace("${user.dir}", System.getProperty("user.dir"));
        // 查询目录下pdf文件
        List<PdfFile> pdfFiles = this.getPdfFiles(pdfPath);
        PageResult<List<PdfFile>> result = new PageResult<>();
        result.setRows(pdfFiles);
        result.setTotal(pdfFiles.size());
        resp.getWriter().write(JSON.toJSONString(result));
    }

    private List<PdfFile> getPdfFiles(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            throw new RuntimeException("pdf文件目录路径不能为空！");
        }
        File file = FileUtil.file(dirPath);
        // 获取到pdf文件
        List<File> files = loopPdfOrOfd(file);
        List<String> fileNames = files.stream().map(File::getName).collect(Collectors.toList());
        // 设置文件名
        List<PdfFile> pdfFiles = fileNames.stream().map(name -> {
            PdfFile pdfFile = new PdfFile(name);
            // 这里模拟数据库中已存在fileId 和 文件名存在情况
            String fileId = UUID.randomUUID().toString().replaceAll("-", "");
            pdfFile.setFileId(fileId);
            pdfFile.setStampName(fileId + name.substring(name.lastIndexOf(".")));
            // 模拟数据库已存在fileId
            BatchStampSealServiceImpl.map.put(name, fileId);
            return pdfFile;
        }).collect(Collectors.toList());
        return pdfFiles;
    }

    /**
     * 递归获得Jar文件
     *
     * @param file jar文件或者包含jar文件的目录
     * @return jar文件列表
     */
    private static List<File> loopPdfOrOfd(File file) {
        return FileUtil.loopFiles(file, 1, GetFileServlet::isPdfOrOfd);
    }

    private static boolean isPdfOrOfd(File file) {
        if (false == FileUtil.isFile(file)) {
            return false;
        }
        return file.getPath().toLowerCase().endsWith(".pdf");
    }
}
