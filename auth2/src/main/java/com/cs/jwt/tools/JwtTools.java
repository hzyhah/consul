package com.cs.jwt.tools;

import com.alibaba.fastjson.JSONObject;
import com.cs.common.json.JsonUtils;
import com.cs.jwt.exception.JwtException;
import com.cs.jwt.exception.JwtOutOfDateException;
import com.cs.common.encryption.RSAEncrypt;
import org.apache.commons.codec.binary.Base64;

import java.util.Map;
import java.util.TreeMap;

public class JwtTools implements JwtModel {

    public Map header(String alg) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("alg", alg);
        map.put("typ", TYP_JWT);
        return map;
    }

    public Map payload(String sub,String aud,Long exp) {
        TreeMap<String, Object> map = new TreeMap<>();
        //发布时间
        map.put("iat", getNow()+"");
        //过期时间
        String expStr =exp==null?DEFAULT_EXP+"":exp+"";
        map.put("exp", expStr);
        //面向的用户(jwt所面向的用户)
        sub=sub==null?DEFAULT_SUB:sub;
        map.put("sub", sub);
        //接收jwt的一方
        aud=aud==null?DEFAULT_AUD:aud;
        map.put("aud", aud);
        return map;
    }

    @Override
    public String compact(String public_key,String sub,String aud,Long exp) throws Exception {
        String headerStr = base64Encode(JsonUtils.toJsonString(header(TYPE_RSA)));
        String payloadStr = base64Encode(JsonUtils.toJsonString(payload(sub,aud,exp)));
        String alg = "RSA";
        String signature ="";
        if (alg.equalsIgnoreCase("RSA")){
            signature = getSign(headerStr,payloadStr,public_key);
        }else if (alg.equalsIgnoreCase("HS256")){

        }
        return headerStr+SEPARATOR+payloadStr+SEPARATOR+signature;
    }

    @Override
    public String compact(String sub, String aud, Long exp) throws Exception {
        if (sub.equals("anonymous")){

        }
        return null;
    }

    @Override
    public void checkToken(String token,String secret) throws JwtException, JwtOutOfDateException {
        if (token==null || token.equals(""))
            throw new JwtException("invalid token");
        String checkSignStr = "";

        try {
            checkSignStr = this.getSign(cutToken("header",token),cutToken("payload",token),secret);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JwtException("build token fail，because of some unknow reason");
        }
        String sing = cutToken("sign",token);
        if (!cutToken("sign",token).equals(checkSignStr)){
            throw new JwtException("invalid token");
        }

        //校验时间
        long iat = Long.valueOf(analysisToken("iat",token));
        long exp = Long.valueOf(analysisToken("exp",token));
        long now = getNow();
        if ((iat+exp)<now|| now< (iat+exp)){
            throw new JwtOutOfDateException("the token is out of date");
        }
    }

    public static long getNow(){
        return System.currentTimeMillis();
    }

    public static String base64Encode(String str){
        return Base64.encodeBase64String(str.getBytes());
    }

    public static String base64Decode(String str){
        return new String(Base64.decodeBase64(str));
    }

    private String getSign(String headerStr,String payloadStr,String public_key) throws Exception {
        RSAEncrypt rsaEncrypt = new RSAEncrypt();
        return rsaEncrypt.encrypt(headerStr+SEPARATOR+payloadStr+SALT,public_key);
    }

    public String getPublicKey(){
        String pk = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdmfepZJQlkfGTRxO1b8j3klM27Ad79T4pUEaZAVcgeSFdamjMul6bhdrB7O9Pori5fQmG7XTBbUAhuMLHCSSYnnoI2CiTKySk0+/lgOCP3UO5oR50fvAfhYvHEw4oeSoSzUfSyzgRvoGdsub3JHP+/3DX/nogbNiq3IWT9S3newIDAQAB";
        return pk;
    }

    public String getPrivateKey(){
        String sercet = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ2Z96lklCWR8ZNHE7VvyPeSUzbsB3v1PilQRpkBVyB5IV1qaMy6XpuF2sHs70+iuLl9CYbtdMFtQCG4wscJJJieegjYKJMrJKTT7+WA4I/dQ7mhHnR+8B+Fi8cTDih5KhLNR9LLOBG+gZ2y5vckc/7/cNf+eiBs2KrchZP1Led7AgMBAAECgYBqhY6np5uaaTJm4Tuo/o1PCE/GZ3IHjpMdSWzuLcG96SFsCmgbODyJ0ccyG/sEUG+ar7CoygaIVldqc37zGHLqwh57S+d6qgWO8PvprWGiMdjDOIA6DYHyhPU0Ek9ucZAlkWQh2QEEnChE0W7EflhOPvDf8OVuSuSpcndXJID/wQJBAOq4Oy/zFEMeowa524N8Kt58fXDi999xHQccXfpuvhl8Yu1tgLIN+kAWvAkqnfx6zCkMXOhs9OKuxYG0Vnsm6v8CQQCr49jJKHdE3GP3OMBgi74Rn/PqrQYXUAkQxMywHNhcFJBJlMPxqeXDDCQ7E3dFfU9eU6hujy+bQNluaotQLy+FAkAHVM/MdJw6aHeRGzcHCVbRcjP81aoGWaMdeL5atyDK7P6uCdCOPy+E1vDynFko7LD5y0APpm+TsP/MSFS3LgUDAkA8kh+0UvQkad04IuF3pAaoQ0s6qRn6YROwuwi+DowXdo+ZvjiGEi6K2t5xitx+ujebr7Mssnw+I037YMQSz23ZAkAfjgSjFeJwH92QSdZ2gfjEU562kFIW4w+O+EDo489jbookISJ3ZqnQ2mOmum5k7057sXyjkmhfd9PM2mnZw8eO";
        return sercet;
    }

    public static String analysisToken(String key,String token) throws JwtException {
        if (key.equals("alg") || key.equals("typ")){
            String header = cutToken("header",token);
            JSONObject jo = JsonUtils.parseObject(base64Decode(header));
            return jo.getString(key);
        }

        if (key.equals("iat") || key.equals("exp") || key.equals("sub") || key.equals("aud")){
            String payload = cutToken("payload",token);
            JSONObject jo = JsonUtils.parseObject(base64Decode(payload));
            return jo.getString(key);
        }

        if (key.equals("sign")){
            return cutToken("sign",token);
        }

        throw new JwtException("invalid token");
    }

    public static String cutToken(String part,String token) throws JwtException {
        String[] tokenStr = token.split("\\.");
        if (part.equalsIgnoreCase("header")){
            return tokenStr[0];
        }

        if (part.equalsIgnoreCase("payload")){
            return tokenStr[1];
        }

        if (part.equalsIgnoreCase("sign")){
            return tokenStr[2];
        }
        throw new JwtException("invalid token");
    }

}
