package com.kinggrid.scan.seal.model;

import java.io.Serializable;

/**
 * 请求网关token
 * @author xionglei
 * @version 1.0.0
 * 2021年09月08日 17:31
 */
public class Token implements Serializable {

    private String token;
    private String checkCode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}
