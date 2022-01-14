package com.kinggrid.scan.seal.model;

/**
 * 传的文档
 * @author xionglei
 * @version 1.0.0
 * 2021年09月10日 16:37
 */
public class Document {
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
}
