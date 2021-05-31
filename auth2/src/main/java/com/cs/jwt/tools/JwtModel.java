package com.cs.jwt.tools;

import com.cs.jwt.exception.JwtException;
import com.cs.jwt.exception.JwtOutOfDateException;

public interface JwtModel {
    public static final String TYPE_HS256="HS256";
    public static final String TYPE_RSA="RSA";
    public static final String TYP_JWT="jwt";
    public static final long DEFAULT_EXP = 30*60*1000;
    public static final String DEFAULT_SUB="ALL";
    public static final String DEFAULT_AUD="www.yourdomain.com";
    public static final String SALT="!_+@()#";
    public static final String SEPARATOR=".";

    public String compact(String public_key,String sub,String aud,Long exp) throws Exception;
    public String compact(String sub,String aud,Long exp) throws Exception;
    public void checkToken(String token,String secret) throws JwtException, JwtOutOfDateException;
}
