package com.kinggrid.scan.seal.model;

import java.io.Serializable;

/**
 * 获取签名值响应
 * @author xionglei
 * @version 1.0.0
 * 2021年09月11日 15:54
 */
public class SignedValueResult implements Serializable {
    /**
     * 文档id
     */
    private String documentId;
    /**
     * 签名值
     */
    private String sigData;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getSigData() {
        return sigData;
    }

    public void setSigData(String sigData) {
        this.sigData = sigData;
    }
}
