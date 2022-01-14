package com.kinggrid.scan.seal.model;

import java.io.Serializable;

/**
 * 扫码后获取扫码用户信息参数
 * @author xionglei
 * @version 1.0.0
 * 2021年09月11日 11:45
 */
public class QrUserGetVo implements Serializable {
    /**
     * 二维码ID
     */
    private String qrcodeId;

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId;
    }
}
