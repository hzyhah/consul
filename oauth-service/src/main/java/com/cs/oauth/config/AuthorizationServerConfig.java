package com.cs.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 授权服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //令牌放置再内存中
        clients.inMemory()
        .withClient("admin")
        .secret(passwordEncoder.encode("123"))
        .accessTokenValiditySeconds(3600)
                //授权成功后跳转
        .redirectUris("https://baidu.com")
        .scopes("all")
                //授权模式grant-type  -- 授权码模式
        .authorizedGrantTypes("authorization_code");

    }
}
