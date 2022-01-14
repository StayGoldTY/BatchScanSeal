package com.kinggrid.scan.seal.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.KGitextpdf.text.DocumentException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kinggrid.encrypt.KGBase64;
import com.kinggrid.pdf.DigitalSignatureByKeySM2;
import com.kinggrid.pdf.KGPdfHummer;
import com.kinggrid.pdf.executes.PdfSignature4SM2;
import com.kinggrid.scan.seal.constant.HttpUrl;
import com.kinggrid.scan.seal.model.*;
import com.kinggrid.scan.seal.service.BatchStampSealService;
import com.kinggrid.scan.seal.utils.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kg.bouncycastle.util.encoders.Hex;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 批量盖章相关接口
 * @author xionglei
 * @version 1.0.0
 * 2021年09月11日 11:33
 */
public class BatchStampSealServiceImpl implements BatchStampSealService {

    private static final Logger logger = Logger.getLogger(BatchStampSealServiceImpl.class);

    /**
     * demo中将盖章后的pdf放到 原pdf所在路径的/stamp/目录下
     */
    public static final String stampPdfDir = "/stamp/";

    /**
     * 文件映射容器
     * 线上可以不需要这个，自己存储文件和盖章文件的隐射关系
     */
    public static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(16);


    @Override
    public QrcodeGetResult qrcodeGet(QrcodeDataVo qrcodeDataVo) {
        if (qrcodeDataVo == null || StringUtils.isBlank(qrcodeDataVo.getWay())) {
            throw new RuntimeException("获取二维码用途不能为空");
        }
        if (StringUtils.isBlank(qrcodeDataVo.getToken()) || StringUtils.isBlank(qrcodeDataVo.getCheckCode())) {
            throw new RuntimeException("获取二维码token和checkCode不能为空");
        }
        Properties properties = PropertiesUtil.getProperties();
        // 获取二维码
        String gatewayUrl = properties.getProperty("gatewayUrl");
        String path = gatewayUrl + HttpUrl.qrcodeGet;
        try {
            String body = HttpUtil.post(path + "?token="+ qrcodeDataVo.getToken() + "&" + "checkCode=" + qrcodeDataVo.getCheckCode(), JSON.toJSONString(qrcodeDataVo));
            Result<QrcodeGetResult> qrcodeGetResultResult = JSON.parseObject(body, new TypeReference<Result<QrcodeGetResult>>(){});
            if (!StringUtils.equals("0", qrcodeGetResultResult.getCode())) {
                throw new RuntimeException("获取二维码异常：" + qrcodeGetResultResult.getMessage());
            }
            QrcodeGetResult data = qrcodeGetResultResult.getData();
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }

    /*public EstablishSessionResult establishSession(AuthCodeVo authCodeVo) {
        if (StringUtils.isBlank(authCodeVo.getAuthCode())) {
            throw new IllegalArgumentException("授权凭证不能为空！");
        }
        Token token = TokenUtil.getToken();
        // 1.建立会话
        Properties properties = PropertiesUtil.getProperties();
        String gatewayUrl = properties.getProperty("gatewayUrl");
        String establishSessionPath = gatewayUrl + HttpUrl.establishSession;
        String body = HttpUtil.post(establishSessionPath + "?token="+ token.getToken() + "&" + "checkCode=" + token.getCheckCode(), JSON.toJSONString(authCodeVo));
        logger.info("建立会话信息：" + body);
        Result<EstablishSessionResult> sessionResultResult = JSON.parseObject(body, new TypeReference<Result<EstablishSessionResult>>(){});
        if (!StringUtils.equals("0", sessionResultResult.getCode())) {
            throw new RuntimeException("建立会话失败：" + sessionResultResult.getMessage());
        }
        return sessionResultResult.getData();
    }*/

    @Override
    public String sessionLogout(SessionLogoutVo sessionLogoutVo) {
        if (StringUtils.isBlank(sessionLogoutVo.getUserToken())) {
            throw new IllegalArgumentException("会话id不能为空！");
        }
        Token token = TokenUtil.getToken();
        // 1.退出会话
        Properties properties = PropertiesUtil.getProperties();
        String gatewayUrl = properties.getProperty("gatewayUrl");
        String sessionLogoutPath = gatewayUrl + HttpUrl.sessionLogout;
        String body = HttpUtil.post(sessionLogoutPath + "?token="+ token.getToken() + "&" + "checkCode=" + token.getCheckCode(), JSON.toJSONString(sessionLogoutVo));
        logger.info("退出会话信息：" + body);
        Result<String> sessionLogoutResult = JSON.parseObject(body, new TypeReference<Result<String>>(){});
        if (!StringUtils.equals("0", sessionLogoutResult.getCode())) {
            throw new RuntimeException("退出会话失败：" + sessionLogoutResult.getMessage());
        }
        return sessionLogoutResult.getCode();
    }

    @Override
    public GetUserBySessionResult getUserBySession(GetUserBySessionVo getUserBySessionVo) {
        if (StringUtils.isBlank(getUserBySessionVo.getUserToken())) {
            throw new IllegalArgumentException("会话id不能为空！");
        }
        if (StringUtils.isBlank(getUserBySessionVo.getCategory())) {
            throw new IllegalArgumentException("印章分类不能为空！");
        }
        Token token = TokenUtil.getToken();
        // 1.获取到印章和用户信息通过会话模式
        Properties properties = PropertiesUtil.getProperties();
        String gatewayUrl = properties.getProperty("gatewayUrl");
        String getUserBySessionPath = gatewayUrl + HttpUrl.getUserBySession;

        String body = HttpUtil.post(getUserBySessionPath + "?token="+ token.getToken() + "&" + "checkCode=" + token.getCheckCode(), JSON.toJSONString(getUserBySessionVo));
        logger.info("通过会话模式查询印章列表：" + body);
        Result<GetUserBySessionResult> getUserBySessionResult = JSON.parseObject(body, new TypeReference<Result<GetUserBySessionResult>>(){});
        if (!StringUtils.equals("0", getUserBySessionResult.getCode())) {
            throw new RuntimeException("会话模式获取用户信息和印章失败：" + getUserBySessionResult.getMessage());
        }
        GetUserBySessionResult data = getUserBySessionResult.getData();
        return data;
    }

    /**
     * 在nacos 中 iSignatureScan-product.properties 配置中配置
     * 1非会话模式，每次签章需要扫码（默认）;2会话模式
     * system.session=2
     * @param qrcodeId 二维码id
     * @return
     */
    @Override
    public GetUserResult qrUserGet(String qrcodeId) {
        if (StringUtils.isBlank(qrcodeId)) {
            throw new RuntimeException("二维码id不能为空");
        }
        Token token = TokenUtil.getToken();
        // 1.获取到印章和用户信息
        Properties properties = PropertiesUtil.getProperties();
        String gatewayUrl = properties.getProperty("gatewayUrl");
        String getUserPath = gatewayUrl + HttpUrl.getUser;

        String body = HttpUtil.post(getUserPath + qrcodeId + "?token="+ token.getToken() + "&" + "checkCode=" + token.getCheckCode(), (String) null);
        logger.info("印章列表：" + body);
        Result<GetUserResult> getUserResultResult = JSON.parseObject(body, new TypeReference<Result<GetUserResult>>(){});
        if (!StringUtils.equals("0", getUserResultResult.getCode())) {
            throw new RuntimeException("请求用户印章信息异常：" + getUserResultResult.getMessage());
        }
        GetUserResult userResult = getUserResultResult.getData();
        return userResult;
    }

    @Override
    public List<SignedValueResult> qrcodeSign(List<Document>documents, QrcodeSignVo qrcodeSignVo) {
        if (CollectionUtil.isEmpty(documents)) {
            throw new IllegalArgumentException("批量文件不能为空");
        }
        if (ObjectUtils.isEmpty(qrcodeSignVo)) {
            throw new IllegalArgumentException("请求获取签名值相关参数不能为空！");
        }
        Token token = TokenUtil.getToken();
        // 请求网关配置
        Properties properties = PropertiesUtil.getProperties();
        String url = properties.getProperty("gatewayUrl");
        // 2.请求获取签名值接口
        String qrcodeSignPath = url + HttpUrl.qrcodeSign;
        try {
            // qrcodeSignVo.setScanId(scanId);
            qrcodeSignVo.setDocuments(documents);
            // qrcodeSignVo.setEsid(esid);
            // 请求签名值结果
            String qrcodeSignBody = HttpUtil.post(qrcodeSignPath + "?token="+ token.getToken() + "&" + "checkCode=" + token.getCheckCode(), JSON.toJSONString(qrcodeSignVo));
            logger.info("获取签名值返回数据：" + qrcodeSignBody);
            Result<SignedValues> signedValueResult = JSON.parseObject(qrcodeSignBody, new TypeReference<Result<SignedValues>>(){});
            String code = signedValueResult.getCode();
            if (!StringUtils.equals((String)code, "0")) {
                throw new RuntimeException("请求获取签名值异常：" + signedValueResult.getMessage());
            }
            SignedValues signedValues = signedValueResult.getData();
            return signedValues.getSignedValues();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    /**
     * 单个文档电子签章接口
     * @param inputStream 原文档流
     * @param outputStream 生成签章后文件流，没有签名值
     * @param sealData 印章数据
     * @return 返回盖章后文件相关信息
     * @throws IOException
     */
    public DigitalSignatureByKeySM2 stampOneFile(InputStream inputStream, OutputStream outputStream, byte[] sealData,String position) throws IOException {
        KGPdfHummer hummer = null;
        DigitalSignatureByKeySM2 digitalSignatureByKeySM2 = null;
        File pdfFile = null;
        try {

            Properties properties = PropertiesUtil.getProperties();
            String pdfFilePath = properties.getProperty("pdf.path");
            pdfFilePath = pdfFilePath.replace("${user.dir}", System.getProperty("user.dir"));
            pdfFile = new File(pdfFilePath + "/inputFile.tmp");
            // 将原文件写到临时文件中
            Files.copy(inputStream, pdfFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            hummer = KGPdfHummer.createSignature(pdfFile.getAbsolutePath(), null, true, outputStream, null, true);
            // 设置签章
            hummer.setGBGMSeal4PdfDigitalSignature(sealData);
            digitalSignatureByKeySM2 = new DigitalSignatureByKeySM2();

            // 获取印章中的签名证书数据
            KGBase64 base64 = new KGBase64();
            Seal seal = SealUtil.getSealAutomatic(sealData);
            String signatureCert = Base64.createBase64().encode(seal.getSignatureCertData());
            byte[] certByte = base64.decode(signatureCert);

            // 设置签名证书
            hummer.setCertificate(getCertificate(certByte), digitalSignatureByKeySM2);

            // 设置盖章位置,以pdf页左下角为坐标原点
            PdfSignature4SM2 pdfSignature4sm2 = new PdfSignature4SM2();
            int positionX = 415;
            int positionY = 260;
            if(position != null && !position.isEmpty()){
                String[] positionArray = position.split(",");
                positionX = Integer.parseInt(positionArray[0]);
                positionY = Integer.parseInt(positionArray[1]);
            }

            pdfSignature4sm2.setXY(positionX, positionY);
            pdfSignature4sm2.setPagen(0);
            hummer.addExecute(pdfSignature4sm2);
            hummer.doSignature();
        } catch (DocumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            hummer.close();
            if (outputStream != null) {
                outputStream.close();
            }
            if (pdfFile != null && pdfFile.exists()) {
                pdfFile.delete();
            }
        }
        return digitalSignatureByKeySM2;
    }

    /**
     * 单个文件电子签章接口
     * @param srcPath 源文件绝对路径
     * @param destPath 目标文件绝对路径
     * @param sealData 印章数据
     * @throws Exception
     * return 返回盖章后文件相关信息
     */
    public DigitalSignatureByKeySM2 stampOneFile(String srcPath, String destPath, byte[] sealData) throws IOException {
        KGBase64 base64 = new KGBase64();
        // 待盖章源文件
        File srcFile = new File(srcPath);
        // 生成的目标文件路径
        File destFile = FileUtil.file(destPath);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        if (!destFile.isFile()) {
            throw new RuntimeException("对单个文件进行盖章异常：" + destPath + "不是文件路径！");
        }
        FileOutputStream fileOutputStream = new FileOutputStream(destPath);
        KGPdfHummer hummer = null;
        DigitalSignatureByKeySM2 digitalSignatureByKeySM2 = null;
        try {
            hummer = KGPdfHummer.createSignature(srcFile.getAbsolutePath(), null, true, fileOutputStream, null, true);
            // 设置签章
            hummer.setGBGMSeal4PdfDigitalSignature(sealData);
            digitalSignatureByKeySM2 = new DigitalSignatureByKeySM2();

            // 获取印章中的签名证书数据
            Seal seal = SealUtil.getSealAutomatic(sealData);
            String signatureCert = Base64.createBase64().encode(seal.getSignatureCertData());
            byte[] certByte = base64.decode(signatureCert);

            // 设置签名证书
            hummer.setCertificate(getCertificate(certByte), digitalSignatureByKeySM2);

            // 设置盖章位置,以pdf页左下角为坐标原点
            PdfSignature4SM2 pdfSignature4sm2 = new PdfSignature4SM2();
            pdfSignature4sm2.setXY(415, 260);
            pdfSignature4sm2.setPagen(1);
            hummer.addExecute(pdfSignature4sm2);
            hummer.doSignature();
        } catch (DocumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            hummer.close();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        return digitalSignatureByKeySM2;

    }

    /**
     * 对盖章后文件进行签名值合成
     * @param digitalSignatureByKeySM2 盖章后文件相关信息
     * @param sigData 签名值
     * @param destPath 生成目标文件路径 和 @link #stampOneFile @param destPath 目标文件绝对路径 一致
     */
    public void composeSigData(DigitalSignatureByKeySM2 digitalSignatureByKeySM2, String sigData, String destPath) {
        KGBase64 base64 = new KGBase64();
        // SM2域签名
        try {
            DigitalSignatureByKeySM2 signatureByKeySM2 = new DigitalSignatureByKeySM2();
            signatureByKeySM2.setSigKeepData(digitalSignatureByKeySM2.getSigKeepData());
            // 签名值
            //byte[] encodedSig = null;
            // 如果是V2结构的章
                   /* if (signatureByKeySM2.getSealVersion() == 2) {
                        encodedSig = GMSesSignature.getGMSesSignature(ASN1Sequence.getInstance(base64.decode(needSigMessage)), base64.decode(sigData)).getEncoded();
                    } else {
                        // 走V4的章
                        ASN1Sequence toSignSeq = ASN1Sequence.getInstance(base64.decode(needSigMessage));
                        byte[] certBytes = base64.decode(signatureCert);
                        // 验证签章人证书
                        ASN1Sequence esealSeq = (ASN1Sequence) toSignSeq.getObjectAt(1);
                        SealGM sealInfo = SealUtil.getSealInfo(esealSeq.getEncoded());
                        X509Certificate certificate = getCertificate(certBytes);
                        VerifyCertUtils.verifySignCert(sealInfo, new Certificate[]{certificate});
                        encodedSig = SesSignature.getSesSignature(toSignSeq, certBytes, base64.decode(sigData), null).getEncoded();
                    }*/
            signatureByKeySM2.write(destPath, Hex.toHexString(base64.decode(sigData)).getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("写入签名值异常：" + e);
        }
    }

    /**
     * 获取证书文件
     *
     * @param certByte
     * @return
     */
    private static X509Certificate getCertificate(byte[] certByte) {
        KGBase64 kgBase64 = new KGBase64();
        logger.info(kgBase64.encode(certByte));
        CertificateFactory factory = null;
        X509Certificate x509Certificate = null;
        try {
            factory = CertificateFactory.getInstance("X.509", "KGBC");
            x509Certificate = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certByte));
        } catch (CertificateException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return x509Certificate;
    }

    /**
     * 递归获得Jar文件
     *
     * @param file jar文件或者包含jar文件的目录
     * @return jar文件列表
     */
    private static List<File> loopPdfOrOfd(File file) {
        return FileUtil.loopFiles(file, 1, BatchStampSealServiceImpl::isPdfOrOfd);
    }

    private static boolean isPdfOrOfd(File file) {
        if (false == FileUtil.isFile(file)) {
            return false;
        }
        return file.getPath().toLowerCase().endsWith(".pdf");
    }
}
