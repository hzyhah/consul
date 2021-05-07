package com.cs.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OauthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthServiceApplication.class, args);
    }

    /**
     * 获取授权码地址
     * http://localhost:9090/oauth/authorize?response_type=code&client_id=admin&redirect_uri=https://baidu.com&scope=all
     */

}
