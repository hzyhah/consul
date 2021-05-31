package com.cs.consulApp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "auth-service")
@RequestMapping(value = "/jwt")
public interface AuthFeign {

    @RequestMapping("/getToken")
    public String getToken(@RequestParam("sub")String sub, @RequestParam("aud")String aud);
}
