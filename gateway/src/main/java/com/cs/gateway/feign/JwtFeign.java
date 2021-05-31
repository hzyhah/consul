package com.cs.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "auth-service")
public interface JwtFeign {
    @RequestMapping("/jwt/check")
    public String check(@RequestParam("token")String token);
}
