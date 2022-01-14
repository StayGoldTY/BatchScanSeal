package com.kinggrid.scan.seal.model;

import java.io.Serializable;

/**
 * 用户信息
 * @author xionglei
 * @version 1.0.0
 * 2021年11月11日 14:32
 */
public class User implements Serializable{
    private static final long serialVersionUID = 5858591583466798137L;

    private String userId;
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
