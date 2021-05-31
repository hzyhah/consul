package com.cs.jwt.tools;

import org.springframework.beans.factory.annotation.Autowired;

public class Jwt {

    @Autowired
    JwtTools jwtTools;

    public String token(String sub,String aud){
        return "";
    }
}
