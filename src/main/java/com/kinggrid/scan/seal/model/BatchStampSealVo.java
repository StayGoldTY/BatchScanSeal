package com.kinggrid.scan.seal.model;

/**
 * 批量盖章参数
 * @author xionglei
 * @version 1.0.0
 * 2021年11月11日 14:59
 */
public class BatchStampSealVo {

    /**
     * 会话模式标识
     * 0: 非会话模式
     * 1: 会话模式
     */
    private String sessionMode;
    /**
     * 二维码id
     * 非会话模式必传
     */
    private String qrcodeId;

    /**
     * 印章分类：
     * 1个人印章
     * 2企业印章
     * 3授权印章
     * 会话模式必传
     */
    private String category;

    /**
     * 统一社会信用代码（企业印章必填）
     * 会话模式必传
     */
    private String entityCode;

    public String getSessionMode() {
        return sessionMode;
    }

    public void setSessionMode(String sessionMode) {
        this.sessionMode = sessionMode;
    }

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }
}
