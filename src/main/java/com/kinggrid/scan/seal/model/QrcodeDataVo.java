package com.kinggrid.scan.seal.model;

import java.io.Serializable;

public class QrcodeDataVo extends Token implements Serializable {
    private static final long serialVersionUID = -1743522687174698996L;
    public static final String WAY_STAMP = "1";
    public static final String WAY_DEL = "2";
    public static final String TYPE_PERSONAL = "1";
    public static final String TYPE_COMPANY = "2";

    /**
     * 二维码用途不能为空！
     * 1：签章
     * 2：删除签章
     */
    private String way;
    /**
     * 印章类别，签章时必传
     * 1：个人签名
     * 2：企业印章，默认
     */
    private String type;
    /**
     * 印章esid，删章时必传
     */
    private String esid;

    public QrcodeDataVo() {
    }

    public String getEsid() {
        return this.esid;
    }

    public void setEsid(String esid) {
        this.esid = esid;
    }

    public String getWay() {
        return this.way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
