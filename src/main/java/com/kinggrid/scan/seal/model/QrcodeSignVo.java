package com.kinggrid.scan.seal.model;

import java.util.List;

/**
 * 批量盖章请求签名值
 * @author xionglei
 * @version 1.0.0
 * 2021年09月01日 17:01
 */
public class QrcodeSignVo {

    /**
     * 印章esid
     * 会话模式必传
     */
    private String esid;

    /**
     * 扫码ID，
     * 非会话模式时，长轮询和短轮询时必传
     */
    private String scanId;

    /**
     * 用户会话ID，会话机制时必传
     * 默认不是会话模式
     */
    private String userToken;


    /**
     * 原文摘要值的Base64编码
     * 国密印章SM3摘要算法
     */
    private String contentBase64;

    /**
     * 签名属性，用于签章结构体
     */
    private String propertyInfo;

    /**
     * 文档id
     */
    private String documentId;

    /**
     * 文档名称
     */
    private String documentName;

    /**
     * 批量获取签名值时传入
     */
    private List<Document> documents;


    /**
     * app终端
     * 1：WEB
     * 2：APP
     * 3：H5
     * 4：微信小程序
     * 5：支付宝小程序
     * 6：钉钉
     */
    private String terminal;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getEsid() {
        return esid;
    }

    public void setEsid(String esid) {
        this.esid = esid;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getContentBase64() {
        return contentBase64;
    }

    public void setContentBase64(String contentBase64) {
        this.contentBase64 = contentBase64;
    }

    public String getPropertyInfo() {
        return propertyInfo;
    }

    public void setPropertyInfo(String propertyInfo) {
        this.propertyInfo = propertyInfo;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
