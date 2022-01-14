package com.kinggrid.scan.seal.utils;

import com.alibaba.fastjson.JSONObject;
import com.kinggrid.scan.seal.model.Seal;
import org.apache.commons.lang3.StringUtils;
import org.kg.bouncycastle.asn1.*;
import org.kg.bouncycastle.asn1.x500.X500Name;
import org.kg.bouncycastle.asn1.x509.Certificate;
import org.kg.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 解析印章数据
 * 
 * @author kg
 *
 */
public class SealUtil {
	private static String convertDate(String dateTime) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = formatter.parse(dateTime);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return simpleDateFormat.format(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getPublicKey(byte[] certData) throws IOException {
		Base64 base64 = Base64.createBase64();
		JSONObject certObj = parseCert(certData);
		byte[] publicKey = certObj.getBytes("publicKey");
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		messageDigest.update(publicKey);
		byte[] hash = messageDigest.digest();
		return base64.encode(hash);
	}

	private static JSONObject parseCert(byte[] in) throws IOException {
		JSONObject object = new JSONObject();
		ASN1InputStream aIn = new ASN1InputStream(in);
		ASN1Sequence seq = (ASN1Sequence) aIn.readObject();
		aIn.close();
		Certificate certificate = Certificate.getInstance(seq);
		SubjectPublicKeyInfo subjectPublicKeyInfo = certificate.getSubjectPublicKeyInfo();
		DERBitString bitString = subjectPublicKeyInfo.getPublicKeyData();
		byte[] publicKeyData = bitString.getBytes();
		object.put("publicKey", publicKeyData);
		return object;
	}

	/**
	 * 解析印章数据自动判断是国密还是国办
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static Seal getSealAutomatic(byte[] bytes) throws IOException {
		ASN1InputStream asn1InputStream = new ASN1InputStream(bytes);
		try {
			ASN1Sequence sequence = (ASN1Sequence) asn1InputStream.readObject();
			asn1InputStream.close();
			// 印章信息
			ASN1Sequence signInfo = (ASN1Sequence) sequence.getObjectAt(0);
			//
			DLSequence signHeader = (DLSequence) signInfo.getObjectAt(0);
			DERInteger signType = (DERInteger) signHeader.getObjectAt(1);
			Seal seal = new Seal();
			if ("4".equals(signType.toString())) {
				// 政务章
				String esID = signInfo.getObjectAt(1).toString();

				// 印章属性
				DLSequence propertyInfo = (DLSequence) signInfo.getObjectAt(2);
				DERInteger sealType = (DERInteger) propertyInfo.getObjectAt(0);
				DERUTF8String sealName = (DERUTF8String) propertyInfo.getObjectAt(1);
				DLSequence certList = (DLSequence) propertyInfo.getObjectAt(3);
				DEROctetString cert = (DEROctetString) certList.getObjectAt(0);
				// 制作日期
				String createDate = ((DERGeneralizedTime) propertyInfo.getObjectAt(4)).getTimeString().substring(0, 14);
				// 有效起始日期
				String validStart = ((DERGeneralizedTime) propertyInfo.getObjectAt(5)).getTimeString().substring(0, 14);
				// 有效终止日期
				String validEnd = ((DERGeneralizedTime) propertyInfo.getObjectAt(6)).getTimeString().substring(0, 14);

				// 印章图像数据
				DLSequence picInfo = (DLSequence) signInfo.getObjectAt(3);
				DERIA5String sealPicType = (DERIA5String) picInfo.getObjectAt(0);
				DEROctetString sealPicData = (DEROctetString) picInfo.getObjectAt(1);
				DERInteger sealPicWidth = (DERInteger) picInfo.getObjectAt(2);
				DERInteger sealPicHeight = (DERInteger) picInfo.getObjectAt(3);
				// 制章者证书
				DEROctetString signCertInfo = (DEROctetString) sequence.getObjectAt(1);

				seal.setEsid(esID);
				seal.setSealType1(sealType.toString());
				seal.setSealName(sealName.toString());
				seal.setWidth(Integer.parseInt(sealPicWidth.toString()));
				seal.setHeight(Integer.parseInt(sealPicHeight.toString()));
				seal.setCreateDate(convertDate(createDate));
				seal.setValidStart(convertDate(validStart));
				seal.setValidEnd(convertDate(validEnd));
				seal.setProductCertData(signCertInfo.getOctets());
				seal.setSignatureCertData(cert.getOctets());
				seal.setSealData(sealPicData.getOctets());
				seal.setSealPicType(sealPicType.getString());
				seal.setSignVersion(signType.toString());
				// esid为14位数字
				// esid与证书cn值一致
				String esid = getEsidFromCert(cert.getOctets());
				if(esID.length() == 14 && StringUtils.isNumeric(esID) && esID.equals(esid)){
					seal.setSealType("GB");
				}else{
					seal.setSealType("GM");
				}
			} else {
				String esID = signInfo.getObjectAt(1).toString();
				// 印章属性
				DLSequence propertyInfo = (DLSequence) signInfo.getObjectAt(2);
				DERInteger sealType = (DERInteger) propertyInfo.getObjectAt(0);
				DERUTF8String sealName = (DERUTF8String) propertyInfo.getObjectAt(1);
				DLSequence certList = (DLSequence) propertyInfo.getObjectAt(2);
				DEROctetString cert = (DEROctetString) certList.getObjectAt(0);
				String PublicKeyHash = getPublicKey(cert.getOctets());
				// 制作日期
				String createDate = ((ASN1UTCTime) propertyInfo.getObjectAt(3)).toString().substring(0, 12);
				// 有效起始日期
				String validStart = ((ASN1UTCTime) propertyInfo.getObjectAt(4)).toString().substring(0, 12);
				// 有效终止日期
				String validEnd = ((ASN1UTCTime) propertyInfo.getObjectAt(5)).toString().substring(0, 12);

				// 印章图像数据
				DLSequence picInfo = (DLSequence) signInfo.getObjectAt(3);
				DERIA5String sealPicType = (DERIA5String) picInfo.getObjectAt(0);
				DEROctetString sealPicData = (DEROctetString) picInfo.getObjectAt(1);
				DERInteger sealPicWidth = (DERInteger) picInfo.getObjectAt(2);
				DERInteger sealPicHeight = (DERInteger) picInfo.getObjectAt(3);
				// 制章者证书
				DEROctetString signCertInfo = (DEROctetString) ((DLSequence) sequence.getObjectAt(1)).getObjectAt(0);

				seal.setEsid(esID);
				seal.setSealType1(sealType.toString());
				seal.setSealName(sealName.toString());
				seal.setWidth(Integer.parseInt(sealPicWidth.toString()));
				seal.setHeight(Integer.parseInt(sealPicHeight.toString()));
				seal.setCreateDate(convertDate("20" + createDate));
				seal.setValidStart(convertDate("20" + validStart));
				seal.setValidEnd(convertDate("20" + validEnd));
				seal.setProductCertData(signCertInfo.getOctets());
				seal.setSignatureCertData(cert.getOctets());
				seal.setSealData(sealPicData.getOctets());
				seal.setPublicKeyHash(PublicKeyHash);
				seal.setSealPicType(sealPicType.getString());
				seal.setSignVersion(signType.toString());
				seal.setSealType("GM");
			}
			return seal;
		} finally {
			if (asn1InputStream != null) {
				asn1InputStream.close();
			}
		}
	}
	
	/**
	 * 从证书获取esid
	 * @param cert
	 * @return
	 * @throws IOException
	 */
	private static String getEsidFromCert(byte[] cert) throws IOException{
		ASN1InputStream aIn = new ASN1InputStream(cert);
		ASN1Sequence seq = (ASN1Sequence) aIn.readObject();
		aIn.close();
		Certificate certificate = Certificate.getInstance(seq);
		X500Name subject = certificate.getSubject();
		String esid = getCN(subject.toString());
		
		return esid;
	}
	
	private static String getCN(String str) {
		String upper = str.toUpperCase();
		int index = upper.indexOf("CN=");
		int spIndex = upper.indexOf(",", index);
		if (spIndex == -1)
			spIndex = upper.indexOf("+", index);
		if (spIndex != -1) {
			return str.substring(index + 3, spIndex);
		} else {
			return str.substring(index + 3);
		}
	}
}
