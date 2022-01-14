package com.kinggrid.scan.seal.servlet;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.kinggrid.pdf.DigitalSignatureByKeySM2;
import com.kinggrid.scan.seal.model.*;
import com.kinggrid.scan.seal.service.impl.BatchStampSealServiceImpl;
import com.kinggrid.scan.seal.utils.Base64;
import com.kinggrid.scan.seal.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kg.bouncycastle.asn1.ASN1Sequence;
import org.kg.bouncycastle.asn1.DERBitString;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 批量盖章请求
 *
 * @author xionglei
 * @version 1.0.0
 * 2021年09月10日 14:41
 */
public class BatchScanSealServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(BatchScanSealServlet.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 批量盖章
            String sessionMode = req.getParameter("sessionMode");
            // 1. 轮询请求用户在码上办事app上选择的印章
            String qrcodeId = req.getParameter("qrcodeId");
            //获取带盖章的文件路径
            String pdfDirPath = req.getParameter("FilePath");

            //获取带盖章的文件的XY坐标值
            String position = req.getParameter("Position");
            /**
             * 印章分类：
             * 1个人印章
             * 2企业印章
             * 3授权印章
             */
            String category = req.getParameter("category");
            // 统一社会信用代码（企业印章必填）
            String entityCode = req.getParameter("entityCode");

            BatchStampSealVo batchStampSealVo = new BatchStampSealVo();
            batchStampSealVo.setSessionMode(sessionMode);
            batchStampSealVo.setQrcodeId(qrcodeId);
            batchStampSealVo.setCategory(category);
            batchStampSealVo.setEntityCode(entityCode);

            if (StringUtils.equals(sessionMode, "1")) {
                // 是会话模式， userToken 通过在nacos配置会话模式后，在请求获取用户信息建立会话
                if (StringUtils.isBlank(qrcodeId) || StringUtils.isBlank(category)) {
                    throw new IllegalArgumentException("二维码id和印章分类参数不能为空！");
                }
                // 轮序调用盖章
                Properties properties = PropertiesUtil.getProperties();
                // pdf目录
                if(pdfDirPath == null){
                    pdfDirPath = properties.getProperty("pdf.path");
                    pdfDirPath = pdfDirPath.replace("${user.dir}", System.getProperty("user.dir"));
                }

                // 批量盖章
                this.batchStampSeals(batchStampSealVo, pdfDirPath, pdfDirPath + BatchStampSealServiceImpl.stampPdfDir,position);
                Result<String> result = new Result<>();
                result.setCode("0");
                result.setMessage("批量盖章成功！");
                resp.getWriter().write(JSON.toJSONString(result));
            }
            if (!StringUtils.equals(sessionMode, "1")) {
                // 非会话模式
                if (StringUtils.isBlank(qrcodeId)) {
                    throw new IllegalArgumentException("非会话模式二维码id不能为空！，请先获取二维码");
                }
                // 轮序调用盖章
                Properties properties = PropertiesUtil.getProperties();
                // pdf目录
                //String pdfDirPath = properties.getProperty("pdf.path");
                //pdfDirPath = pdfDirPath.replace("${user.dir}", System.getProperty("user.dir"));
                // 批量盖章
                this.batchStampSeals(batchStampSealVo, pdfDirPath, pdfDirPath + BatchStampSealServiceImpl.stampPdfDir,position);
                Result<String> result = new Result<>();
                result.setCode("0");
                result.setMessage("批量盖章成功！");
                resp.getWriter().write(JSON.toJSONString(result));
            }
        } catch (Exception e) {
            Result<String> result = new Result<>();
            result.setCode("-1");
            result.setMessage("批量盖章异常：" + e.getMessage());
            resp.getWriter().write(JSON.toJSONString(result));
        }
    }

    /**
     * 批量盖章
     *
     * @param batchStampSealVo  盖章所属参数，判定会话和非会话
     * @param dirPath   待盖章文件存放的目录
     * @param stampPath 存放已盖章的文件目录
     */
    private void batchStampSeals(BatchStampSealVo batchStampSealVo, String dirPath, String stampPath, String position) {
        if (batchStampSealVo == null || StringUtils.isBlank(dirPath) || StringUtils.isBlank(stampPath)) {
            throw new RuntimeException("参数不能为空!");
        }
        //如果目标路径不存在则创建该路径
        File destfile = new File(stampPath);
        if (!destfile.exists()) {
            destfile.mkdirs();
        }

        BatchStampSealServiceImpl batchStampSealService = new BatchStampSealServiceImpl();
        CommonUserResult commonUserResult = null;

        /**
         * 会话模式所属的会话id
         */
        String userToken = null;
        // 非会话模式
        if (!StringUtils.equals(batchStampSealVo.getSessionMode(), "1")) {
            // 1.选获取用户印章信息
            String qrcodeId = batchStampSealVo.getQrcodeId();
            commonUserResult = batchStampSealService.qrUserGet(qrcodeId);
        } else {
            // 会话模式
            String qrcodeId = batchStampSealVo.getQrcodeId();
            /**
             * 建立会话
             * 这里线上在nacos中 iSignatureScan-product.properties 配置中配置
             * system.session=2 会话模式
             */
            GetUserResult getUserResult = batchStampSealService.qrUserGet(qrcodeId);
            // 这个就是建立会话的会话id
            userToken = getUserResult.getUserToken();
            GetUserBySessionVo getUserBySessionVo = new GetUserBySessionVo();
            getUserBySessionVo.setUserToken(userToken);
            getUserBySessionVo.setCategory(batchStampSealVo.getCategory());
            getUserBySessionVo.setEntityCode(batchStampSealVo.getEntityCode());
            commonUserResult = batchStampSealService.getUserBySession(getUserBySessionVo);
        }


        // 2. 获取待盖章文件
        File pdfFile = FileUtil.file(dirPath);
        // 获取到pdf文件
        List<File> files = loopPdfOrOfd(pdfFile);
        // 这个就是用户选择后返回的需要盖章的印章
        SealDataVo sealDataVo = commonUserResult.getSeals().get(0);
        // 印章esid
        String esid = sealDataVo.getEsid();
        // 获取印章证书
        // 印章sealData base64数据
        String sealDataBase64 = sealDataVo.getSealData();
        byte[] sealData = Base64.createBase64().decode(sealDataBase64);

        // 存储文件盖章后，但没有签名值相关信息
        List<DigitalSignatureByKeySM2> signatureByKeySM2s = new ArrayList<>();
        // 目标文件路径存储
        List<String> destPathList = new ArrayList<>();
        // 文档存储
        List<Document> documentInfoList = new ArrayList();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            try {
                String name = file.getName();
                //TODO 模拟从容器中拿取文件id, 线上请从数据库中拿取fileId
                //String documentId = BatchStampSealServiceImpl.map.get(name);
                //String destFileName = documentId + name.substring(name.lastIndexOf("."));
                String documentId = name;
                String destFileName = name;
                        // 当前目标文件存储路径
                String destPath = stampPath + File.separatorChar + destFileName;


                // 对单个文件进行盖章，但没有签名值
                DigitalSignatureByKeySM2 signatureByKeySM2 = batchStampSealService.stampOneFile(new FileInputStream(file), new FileOutputStream(destPath), sealData,position);
                // DigitalSignatureByKeySM2 signatureByKeySM2 = batchStampSealService.stampOneFile(file.getAbsolutePath(), destPath, sealData);
                // 需要签名的源文件的摘要
                signatureByKeySM2s.add(signatureByKeySM2);
                // 原文摘要值
                String needSigMessage = signatureByKeySM2.getHash();
                // String sigKeepData = signatureByKeySM2.getSigKeepData();
                byte[] needSigMessageDecode = Base64.createBase64().decode(needSigMessage);
                ASN1Sequence instance = ASN1Sequence.getInstance(needSigMessageDecode);
                // 原文摘要值结构体
                DERBitString derBitString = (DERBitString) instance.getObjectAt(3);
                // 原文摘要值base64
                String contentBase64 = Base64.createBase64().encode(derBitString.getBytes());
                Document documentInfo = new Document();
                documentInfo.setDocumentId(documentId);
                documentInfo.setDocumentName(name);
                documentInfo.setContentBase64(contentBase64);
                documentInfoList.add(documentInfo);
                destPathList.add(destPath);

            } catch (Exception e) {
                logger.error("盖章异常：" + e.getMessage(), e);
                throw new RuntimeException("盖章异常：" + e.getMessage());
            }

        }

        // 设置文档
        if (documentInfoList != null && documentInfoList.size() > 0) {
            List<SignedValueResult> sigDataList = null;
            // 非会话模式
            if (!StringUtils.equals(batchStampSealVo.getSessionMode(), "1")) {
                // 请求批量文档签名值
                QrcodeSignVo qrcodeSignVo = new QrcodeSignVo();
                qrcodeSignVo.setScanId(((GetUserResult)commonUserResult).getScanId());
                sigDataList = batchStampSealService.qrcodeSign(documentInfoList, qrcodeSignVo);
            } else {
                // 会话模式,请求会话模式获取签名值
                QrcodeSignVo qrcodeSignVo = new QrcodeSignVo();
                qrcodeSignVo.setUserToken(userToken);
                qrcodeSignVo.setEsid(esid);
                sigDataList = batchStampSealService.qrcodeSign(documentInfoList, qrcodeSignVo);
            }

            for (int i = 0; i < sigDataList.size(); i++) {
                SignedValueResult signedValueResult = sigDataList.get(i);
                // 如果是正式环境可以按照文档id隐射设置签名值
                String sigData = signedValueResult.getSigData();
                DigitalSignatureByKeySM2 digitalSignatureByKeySM2 = signatureByKeySM2s.get(i);
                // 目标文件路径
                String destPath = destPathList.get(i);
                // 对单个盖章后文件进行签名值合成
                batchStampSealService.composeSigData(digitalSignatureByKeySM2, sigData, destPath);
            }
        }
    }

    /**
     * 递归获得Jar文件
     *
     * @param file jar文件或者包含jar文件的目录
     * @return jar文件列表
     */
    private static List<File> loopPdfOrOfd(File file) {
        return FileUtil.loopFiles(file, 1, BatchScanSealServlet::isPdfOrOfd);
    }

    private static boolean isPdfOrOfd(File file) {
        if (false == FileUtil.isFile(file)) {
            return false;
        }
       // return file.getPath().toLowerCase().endsWith(".pdf");
        return true;
    }
}
