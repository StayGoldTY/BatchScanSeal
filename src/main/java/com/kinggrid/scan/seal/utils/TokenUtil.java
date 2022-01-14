package com.kinggrid.scan.seal.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kinggrid.scan.seal.constant.HttpUrl;
import com.kinggrid.scan.seal.model.Token;

import java.util.Properties;

/**
 * 获取token
 * @author xionglei
 * @version 1.0.0
 * 2021年09月10日 15:15
 */
public class TokenUtil {

    /**
     * 获取token
     * @return
     */
    public static Token getToken() {
        try {
            Properties properties = PropertiesUtil.getProperties();
            String gatewayUrl = (String) properties.get("gatewayUrl");
            String appKey = (String) properties.get("appKey");
            String appSecret = (String) properties.get("appSecret");
            String tokenBody = HttpUtil.get(gatewayUrl + HttpUrl.tokenGet + appKey);
            JSONObject tokenObj = (JSONObject) JSON.parse(tokenBody);
            String tokenStr = (String)((JSONObject) tokenObj.get("data")).get("token");
            String checkCode256 = Encrypt.encodeSHA256((tokenStr + appSecret).getBytes("utf-8"));
            String checkCode64 = Base64.createBase64().encode(checkCode256.getBytes("utf-8"));
            Token token = new Token();
            token.setToken(tokenStr);
            token.setCheckCode(checkCode64);
            return token;
        } catch (Exception e) {
            throw new RuntimeException("网关获取token异常：" + e.getMessage(), e);
        }
    }
}
