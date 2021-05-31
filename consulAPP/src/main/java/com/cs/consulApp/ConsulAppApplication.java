package com.cs.consulApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.cs.consulApp.feign"})
public class ConsulAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsulAppApplication.class, args);
    }
}
