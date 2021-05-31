package com.cs.jwt.redis;

public class KeyGenerator {
    public static final String Token_KEY_HEADER="access_token";
    public static final String separator="@";
    public static final String ACCESS_AUTH_HEADER = "access_auth";

    public static String getTokenKey(String sub,String aud){
        return Token_KEY_HEADER+separator+sub+separator+aud;
    }


    public static String getAccessAuthKey(String sub){
        return ACCESS_AUTH_HEADER+separator+sub;
    }
}
