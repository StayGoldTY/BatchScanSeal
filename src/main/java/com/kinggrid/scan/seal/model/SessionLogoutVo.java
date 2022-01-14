package com.kinggrid.scan.seal.model;

/**
 * @author xionglei
 * @version 1.0.0
 * 2021年11月11日 15:55
 */
public class SessionLogoutVo {
    /**
     * 会话id
     */
    private String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
