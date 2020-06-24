package com.example.authserver.config;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author: hy.bu
 */
public class JwtAccessToken extends JwtAccessTokenConverter {
    public static final  String USER_INFO = "USER_INFO";

    /**
     * 生成token
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);

        // 设置额外信息: 增强
        Map<String, String> additionInfo = new HashMap<>();
        additionInfo.put("phone", "15233650828");
        // 将用户信息添加到token额外信息中
        defaultOAuth2AccessToken.getAdditionalInformation().put(USER_INFO, additionInfo);

        Date date = new Date(System.currentTimeMillis() + 300000);
        defaultOAuth2AccessToken.setExpiration(date);

        return super.enhance(defaultOAuth2AccessToken, authentication);
    }

    /**
     * 解析token
     */
    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map){
        OAuth2AccessToken oauth2AccessToken = super.extractAccessToken(value, map);
        convertData(oauth2AccessToken, oauth2AccessToken.getAdditionalInformation());
        return oauth2AccessToken;
    }

    private void convertData(OAuth2AccessToken accessToken,  Map<String, ?> map) {
        accessToken.getAdditionalInformation().put(USER_INFO,convertUserData(map.get(USER_INFO)));
    }

    private Map<String, String> convertUserData(Object map) {
        return (Map<String, String>) map;
    }
}
