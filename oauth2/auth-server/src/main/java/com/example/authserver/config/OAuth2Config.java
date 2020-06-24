package com.example.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Description:
 * Author: hy.bu
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    public UserDetailsService kiteUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenStore redisTokenStore;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

//    配置授权（authorization）以及令牌（token）的访问端点
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
//        endpoints.authenticationManager(authenticationManager) // 支持password模式
//                .userDetailsService(kiteUserDetailsService) // 设置用户验证服务
//                .tokenStore(redisTokenStore); // 指定token的存储方式
        endpoints.authenticationManager(authenticationManager)
                // 配置JwtAccessToken转换器: JWT 有自身独特的数据格式
                .accessTokenConverter(jwtAccessTokenConverter)
                .reuseRefreshTokens(false).
                userDetailsService(kiteUserDetailsService);
    }

//    用来配置客户端详情服务
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("order-client")
                .secret(passwordEncoder.encode("order-secret-8888"))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .redirectUris("https://www.baidu.com")
                .accessTokenValiditySeconds(3600)
                .scopes("all")
                .and()
                .withClient("user-client")
                .secret(passwordEncoder.encode("user-secret-8888"))
                .authorizedGrantTypes("authorization_code", "refresh_token", "password")
                .redirectUris("https://www.baidu.com")
                .accessTokenValiditySeconds(3600)
                .scopes("all");
//        http://127.0.0.1:6001/oauth/authorize?client_id=user-client&response_type=code&redirect_uri=https://www.baidu.com

    }

//    用来配置令牌端点(Token Endpoint)的安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients(); // 允许客户端访问 OAuth2 授权接口 否则请求 token 会返回 401
        security.checkTokenAccess("isAuthenticated()"); // 用户访问 checkToken 接口
        security.tokenKeyAccess("isAnonymous()"); // 允许资源服务器通过HTTP请求来解码令牌

        // "isAnonymous() hasAuthority('ROLE_TRUSTED_CLIENT')
    }
}
