package com.kinggrid.scan.seal.servlet;

import com.alibaba.fastjson.JSON;
import com.kinggrid.scan.seal.model.AuthCodeVo;
import com.kinggrid.scan.seal.model.EstablishSessionResult;
import com.kinggrid.scan.seal.model.Result;
import com.kinggrid.scan.seal.service.impl.BatchStampSealServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 建立会话
 * @author xionglei
 * @version 1.0.0
 * 2021年11月11日 13:57
 */
public class EstablishSessionServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EstablishSessionServlet.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*// 授权凭证
        try {
            String authCode = req.getParameter("authCode");
            BatchStampSealServiceImpl batchStampSealService = new BatchStampSealServiceImpl();
            AuthCodeVo authCodeVo = new AuthCodeVo();
            authCodeVo.setAuthCode(authCode);
            EstablishSessionResult establishSessionResult = batchStampSealService.establishSession(authCodeVo);
            Result<String> result = new Result<>();
            result.setCode("0");
            result.setMessage("建立会话成功！");
            result.setData(establishSessionResult.getToken());
            resp.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            Result<String> result = new Result<>();
            result.setCode("-1");
            result.setMessage("建立会话失败！" + e.getMessage());
            resp.getWriter().write(JSON.toJSONString(result));
        }*/

    }

}
