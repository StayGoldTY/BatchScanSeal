package com.kinggrid.scan.seal.service;

import com.kinggrid.pdf.DigitalSignatureByKeySM2;
import com.kinggrid.scan.seal.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 批量盖章相关接口
 * @author xionglei
 * @version 1.0.0
 * 2021年09月11日 11:12
 */
public interface BatchStampSealService {

    /**
     * 获取二维码相关信息
     * @param qrcodeDataVo 获取二维码参数
     * @return 二维码相关信息
     */
    QrcodeGetResult qrcodeGet(QrcodeDataVo qrcodeDataVo);

    /**
     * 退出会话模式
     * @param sessionLogoutVo 会话id
     * @return 0: 标识成功退出
     */
    String sessionLogout(SessionLogoutVo sessionLogoutVo);

    /**
     * 获取用户信息和印章信息接口【会话专用】
     * @param getUserBySessionVo
     * @return
     */
    GetUserBySessionResult getUserBySession(GetUserBySessionVo getUserBySessionVo);

    /**
     * 获取用户扫码后当前用户、印章信息
     * @param qrcodeId 二维码id
     * @return 获取用户扫码后用户、印章信息
     */
    GetUserResult qrUserGet(String qrcodeId);

    /**
     * 批量请求签名值列表
     * @param documents 盖章后文档列表
     * @param qrcodeSignVo 区分会话和非会话模式相关参数
     * @return
     */
    List<SignedValueResult> qrcodeSign(List<Document> documents, QrcodeSignVo qrcodeSignVo);

    /**
     * 单个文档电子签章接口
     * @param inputStream 原文档流
     * @param outputStream 生成签章后文件流，没有签名值
     * @param sealData 印章数据
     * @return 返回盖章后文件相关信息
     * @throws IOException
     */
    DigitalSignatureByKeySM2 stampOneFile(InputStream inputStream, OutputStream outputStream, byte[] sealData,String position) throws IOException;

        /**
         * 对单个文件进行盖章，但没有签名值
         * @param srcPath 源文件绝对路径
         * @param destPath 生成目标文件绝对路径
         * @param sealData 印章数据
         * @return 返回盖章后文件相关信息
         * @throws IOException
         */
    DigitalSignatureByKeySM2 stampOneFile(String srcPath, String destPath, byte[] sealData) throws IOException;

    /**
     * 对盖章后文件进行签名值合成
     * @param digitalSignatureByKeySM2 盖章后文件相关信息
     * @param sigData 签名值
     * @param destPath 生成目标文件路径 和 @link #stampOneFile @param destPath 目标文件绝对路径 一致
     */
    void composeSigData(DigitalSignatureByKeySM2 digitalSignatureByKeySM2, String sigData, String destPath);

}
