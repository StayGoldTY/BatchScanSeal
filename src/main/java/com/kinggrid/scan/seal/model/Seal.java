package com.kinggrid.scan.seal.model;

import java.io.Serializable;

public class Seal implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 印章标识*/
	private String esid;
	
	/** 印章类型 GM国密、GB国办*/
	private String sealType;
	
	/** 印章名称*/
	private String sealName;
	
	/** 印章宽度(毫米)*/
	private int width;
	
	/** 印章高度(毫米)*/
	private int height;
	
	/** 制作日期,格式:yyyy-MM-dd HH:mm:ss*/
	private String createDate;
	
	/** 有效起始日期,格式:yyyy-MM-dd HH:mm:ss*/
	private String validStart;
	
	/** 有效终止日期,格式:yyyy-MM-dd HH:mm:ss*/
	private String validEnd;
	
	/**制章者证书*/
	private byte[] productCertData;
	
	/**签名证书*/
	private byte[] signatureCertData;

	/**印章数据*/
	private byte[] sealData;

	/**集中式,公钥的hash值base64编码,算法：SHA-256【集中式】*/
	private String publicKeyHash;
	
	/**图片类型 */
	private String sealPicType;
	
	/** 印章类型  印章结构体中定义的印章类型
	 * 电子印章类型格式分为电子公章标识和电子名章标识两类，
	 * 电子印章类型至少包括电子法定名称章(代码：01)、电子财务专用章(代码：02)、电子发票专用章(代码：03)、电子合同专用章（代码：04）、电子名章（代码：05）五类，
	 * 当印章类型代码为 01、02、03、04 时，称为电子公章标识；当印章类型代码为 05 时，称为电子名章标识
	 * */
	private String sealType1;
	/** 印章版本 */
	private String signVersion;

	public String getSignVersion() {
		return signVersion;
	}

	public void setSignVersion(String signVersion) {
		this.signVersion = signVersion;
	}

	public String getSealType1() {
		return sealType1;
	}

	public void setSealType1(String sealType1) {
		this.sealType1 = sealType1;
	}

	public String getEsid() {
		return esid;
	}

	public void setEsid(String esid) {
		this.esid = esid;
	}

	public String getSealType() {
		return sealType;
	}

	public void setSealType(String sealType) {
		this.sealType = sealType;
	}

	public String getSealName() {
		return sealName;
	}

	public void setSealName(String sealName) {
		this.sealName = sealName;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getValidStart() {
		return validStart;
	}

	public void setValidStart(String validStart) {
		this.validStart = validStart;
	}

	public String getValidEnd() {
		return validEnd;
	}

	public void setValidEnd(String validEnd) {
		this.validEnd = validEnd;
	}

	public byte[] getProductCertData() {
		return productCertData;
	}

	public void setProductCertData(byte[] productCertData) {
		this.productCertData = productCertData;
	}

	public byte[] getSignatureCertData() {
		return signatureCertData;
	}

	public void setSignatureCertData(byte[] signatureCertData) {
		this.signatureCertData = signatureCertData;
	}

	public String getPublicKeyHash() {
		return publicKeyHash;
	}

	public void setPublicKeyHash(String publicKeyHash) {
		this.publicKeyHash = publicKeyHash;
	}

	public String getSealPicType() {
		return sealPicType;
	}

	public void setSealPicType(String sealPicType) {
		this.sealPicType = sealPicType;
	}

	public byte[] getSealData() {
		return sealData;
	}

	public void setSealData(byte[] sealData) {
		this.sealData = sealData;
	}
	
}
