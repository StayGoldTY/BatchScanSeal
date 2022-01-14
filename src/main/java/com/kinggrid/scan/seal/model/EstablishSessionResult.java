package com.kinggrid.scan.seal.model;

import java.io.Serializable;

/**
 * 建立会话返回结果
 * @author xionglei
 * @version 1.0.0
 * 2021年11月11日 13:41
 */
public class EstablishSessionResult implements Serializable {
    private static final long serialVersionUID = 5357047049950911094L;

    /**
     * 用户token
     */
    private String token;
    /**
     * 账号类型：
     * 1-自然人
     * 2-法人
     */
    private Integer accountType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
