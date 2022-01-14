package com.kinggrid.scan.seal.constant;

/**
 * 请求接口
 * @author xionglei
 * @version 1.0.0
 * 2021年09月08日 13:53
 */
public class HttpUrl {

    /**
     * 获取token接口
     */
    public static final String tokenGet = "/token/get/";
    /**
     * 获取签名值接口
     */
    public static final String qrcodeSign = "/qrcode/sign";

    /**
     * 获取二维码接口
     */
    public static final String qrcodeGet = "/qrcode/get";

    /**
     *获取赣服通用户和印章信息
     * 参数传在path后 /{}
     */
    public static final String getUser = "/qr/user/get/";

    /**
     * 获取用户信息和印章信息接口【会话专用】
     */
    public static final String getUserBySession = "/qrcode/user/get/seals";

    /**
     * 退出会话模式
     */
    public static final String sessionLogout = "/qrcode/user/logout";


}
