package com.kinggrid.scan.seal.model;

public class PdfFile {

    public PdfFile(String name) {
        this.name = name;
    }

    public PdfFile() {
    }

    /**
     * 源文件名称
     */
    private String name;
    /**
     * 文件id
     */
    private String fileId;
    /**
     * 盖章后文件名称
     * 本demo中将 fileId作为盖章后文件名称
     */
    private String stampName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStampName() {
        return stampName;
    }

    public void setStampName(String stampName) {
        this.stampName = stampName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

}

