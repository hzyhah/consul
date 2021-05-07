package com.cs.apiSer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.cs.apiSer.dao")
public class ApiSerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiSerApplication.class, args);
    }
}
