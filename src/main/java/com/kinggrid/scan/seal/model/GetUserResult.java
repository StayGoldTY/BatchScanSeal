package com.kinggrid.scan.seal.model;

import java.io.Serializable;
import java.util.List;

/**
 * 扫码获取用户印章信息
 * @author xionglei
 * @version 1.0.0
 * 2021年09月09日 09:52
 */
public class GetUserResult extends CommonUserResult implements Serializable {

    private static final long serialVersionUID = 2599126911309264868L;
    /**
     * 扫码ID，长轮询和短轮询时返回
     * （非会话模式） 默认是非会话模式
     */
    private String scanId;

    /**
     * 当在 nacos 中 的iSignatureScan-product.properties配置中配置
     * 1非会话模式，每次签章需要扫码（默认）;2会话模式
     * system.session=2
     * 用户会话ID，会话30分钟有效
     * （会话模式）
     */
    private String userToken;

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

}

