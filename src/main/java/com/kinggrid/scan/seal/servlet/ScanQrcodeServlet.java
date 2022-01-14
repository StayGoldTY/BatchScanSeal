package com.kinggrid.scan.seal.servlet;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kinggrid.scan.seal.constant.HttpUrl;
import com.kinggrid.scan.seal.model.QrcodeDataVo;
import com.kinggrid.scan.seal.model.QrcodeGetResult;
import com.kinggrid.scan.seal.model.Result;
import com.kinggrid.scan.seal.model.Token;
import com.kinggrid.scan.seal.service.impl.BatchStampSealServiceImpl;
import com.kinggrid.scan.seal.utils.PropertiesUtil;
import com.kinggrid.scan.seal.utils.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * 获取二维码图片
 * @author xionglei
 * @version 1.0.0
 * 2021年09月10日 15:02
 */
public class ScanQrcodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * 二维码
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取二维码
        Token token = TokenUtil.getToken();
        // 二维码用途
        String way = req.getParameter("way");
        // 1: 个人印章 2：企业印章
        String type = req.getParameter("type");
        try {
            BatchStampSealServiceImpl batchStampSealService = new BatchStampSealServiceImpl();
            QrcodeDataVo qrcodeDataVo = new QrcodeDataVo();
            qrcodeDataVo.setToken(token.getToken());
            qrcodeDataVo.setCheckCode(token.getCheckCode());
            qrcodeDataVo.setWay(way);
            qrcodeDataVo.setType(type);
            QrcodeGetResult data = batchStampSealService.qrcodeGet(qrcodeDataVo);
            Result<QrcodeGetResult> qrcodeGetResultResult = new Result<>();
            qrcodeGetResultResult.setCode("0");
            qrcodeGetResultResult.setMessage("获取二维码成功！");
            qrcodeGetResultResult.setData(data);
            resp.getWriter().write(JSON.toJSONString(qrcodeGetResultResult));

        } catch (Exception e) {
            Result<String> result = new Result<>();
            result.setCode("-1");
            result.setMessage("获取二维码异常：" + e.getMessage());
            resp.getWriter().write(JSON.toJSONString(result));
        }
    }
}
