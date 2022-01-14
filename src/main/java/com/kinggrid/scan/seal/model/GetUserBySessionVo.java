package com.kinggrid.scan.seal.model;

/**
 * 获取用户信息和印章信息接口【会话专用】
 * @author xionglei
 * @version 1.0.0
 * 2021年11月11日 14:19
 */
public class GetUserBySessionVo {

    /**
     * 会话ID
     */
    private String userToken;

    /**
     * 印章分类：
     * 1个人印章
     * 2企业印章
     * 3授权印章
     */
    private String category;

    /**
     * 统一社会信用代码（企业印章必填）
     */
    private String entityCode;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
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
