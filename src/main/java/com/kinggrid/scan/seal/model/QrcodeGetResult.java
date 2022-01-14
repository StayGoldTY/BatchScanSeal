package com.kinggrid.scan.seal.model;

import java.io.Serializable;

/**
 * 获取二维码结果
 * @author xionglei
 * @version 1.0.0
 * 2021年09月09日 09:18
 */
public class QrcodeGetResult implements Serializable {

    /**
     * 二维码ID（60秒有效）
     */
    private String qrcodeId;
    /**
     * 获取用户和印章信息接口轮询模式
     * 0：长轮询（60秒）
     * 1：短轮询（建议3秒定时发送请求）
     */
    private String qrType;
    /**
     * 二维码图片，金格加密信息Base64数据
     */
    private String image;

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public String getQrType() {
        return qrType;
    }

    public void setQrType(String qrType) {
        this.qrType = qrType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
