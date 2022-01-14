package com.kinggrid.scan.seal.model;

import java.io.Serializable;
import java.util.List;

/**
 * 扫码获取用户印章信息
 * @author xionglei
 * @version 1.0.0
 * 2021年09月09日 09:52
 */
public class CommonUserResult implements Serializable {

    private static final long serialVersionUID = 2599126911309264868L;

    /**
     * 用户信息
     */
    private User userInfo;

    /**
     * 印章信息
     */
    private List<SealDataVo> seals;

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

    public List<SealDataVo> getSeals() {
        return seals;
    }

    public void setSeals(List<SealDataVo> seals) {
        this.seals = seals;
    }


}

