
package com.kinggrid.scan.seal.model;

import java.io.Serializable;

public class SealDataVo implements Serializable {
    private static final long serialVersionUID = 2823841475621203630L;
    private String esid;
    private String sealName;
    private int width;
    private int height;
    private String imageData;
    private String sealData;

    public SealDataVo() {
    }

    public String getSealData() {
        return this.sealData;
    }

    public void setSealData(String sealData) {
        this.sealData = sealData;
    }

    public String getEsid() {
        return this.esid;
    }

    public void setEsid(String esid) {
        this.esid = esid;
    }

    public String getSealName() {
        return this.sealName;
    }

    public void setSealName(String sealName) {
        this.sealName = sealName;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageData() {
        return this.imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
