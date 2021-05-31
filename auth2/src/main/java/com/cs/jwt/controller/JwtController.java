package com.cs.jwt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.cs.api.pojo.AccessAuth;
import com.cs.api.provider.IAccessAuthProvider;
import com.cs.common.CodeMsg;
import com.cs.common.Result;
import com.cs.common.json.JsonUtils;
import com.cs.jwt.exception.JwtException;
import com.cs.jwt.exception.JwtOutOfDateException;
import com.cs.jwt.redis.KeyGenerator;
import com.cs.jwt.tools.JwtTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


@RestController
@RequestMapping("/jwt")
public class JwtController  {

    @Autowired
    Jedis jedis;

    @Reference
    IAccessAuthProvider accessAuthProvider;

    @RequestMapping("/register")
    public String register(String sub,String aud) throws Exception {
        String secret = this.getSecret(sub);
        if (secret==null)
            return Result.error(CodeMsg.NO_ACCESS_AUTH);
        JwtTools jwtTools = new JwtTools();
        String token = jwtTools.compact(secret,sub,aud,JwtTools.DEFAULT_EXP);
        TreeMap treeMap = JsonUtils.parseObject(jwtTools.base64Decode(token.split("\\.")[1]), TreeMap.class);
        treeMap.put("access_token",token);
        String key = KeyGenerator.getTokenKey(sub,aud);
        try{
            jedis.hmset(key,treeMap);
            jedis.expireAt(key,Long.valueOf(treeMap.get("iat").toString())+Long.valueOf(treeMap.get("exp").toString()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.ok(treeMap);
    }


    @RequestMapping("/getToken")
    public String getToken(String sub,String aud) throws Exception {
        String key = KeyGenerator.getTokenKey(sub,aud);
        //缓存中查询access token
        Map<String, String> map = null;
        try {
            map = jedis.hgetAll(key);
        }catch (Exception e){
            e.printStackTrace();
            // 缓存连接异常
        }
        if (map==null || map.isEmpty()){
            return this.register(sub, aud);
        }else{
            long iat = Long.valueOf(map.get("iat").toString());
            long exp = Long.valueOf(map.get("exp").toString());
            long now = JwtTools.getNow();
            if (iat<=now && now<=(iat+exp)){
                return Result.ok(new TreeMap<String, String>(map));
            }else{
                //刷新token
                return this.register(sub,aud);
            }
        }
    }


    @RequestMapping("/check")
    public String check(String token) throws JwtException {
        String sub1 = JwtTools.analysisToken("sub", token);
        String secret = getSecret(sub1);
        if (secret==null)
            return Result.error(CodeMsg.NO_ACCESS_AUTH);
        JwtTools jwtTools = new JwtTools();
        try {
            jwtTools.checkToken(token,secret);
        } catch (JwtException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.ILLEGAL_JTW);
        } catch (JwtOutOfDateException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.OUTOFDATE_JTW);
        }

        return Result.ok();
    }

    @RequestMapping("/refresh")
    public String refresh(String token) throws Exception {
        if (token==null && token.length()==0)
            return Result.error(CodeMsg.ILLEGAL_JTW);
        String msg = this.check(token);
        JSONObject jo = JsonUtils.parseObject(msg);
        if (!jo.get("retCode").toString().equals("1")){
            return msg;
        }

        long iat = Long.valueOf(JwtTools.analysisToken("iat",token));
        long exp = Long.valueOf(JwtTools.analysisToken("iat",token));
        long now = JwtTools.getNow();
        // 还在有效期内
        if (iat<=now && now<=(iat+exp-(5*60*1000))){
            String payloadStr = JwtTools.cutToken("payload",token);
            TreeMap treeMap = JsonUtils.parseObject(JwtTools.base64Decode(payloadStr), TreeMap.class);
            treeMap.put("access_token",token);
            return Result.ok(treeMap);
        }else{
            //刷新token
            String sub = JwtTools.analysisToken("sub", token);
            String aud = JwtTools.analysisToken("aud", token);
            return this.register(sub,aud);
        }

    }

    public Map selectCache(String key){
        //缓存中查询access token
        Map<String, String> map = null;
        try {
            map = jedis.hgetAll(key);
            return map;
        }catch (Exception e){
            e.printStackTrace();
            // 缓存连接异常
        }
        return null;
    }

    public String getSecret(String sub) {
        String key = KeyGenerator.getAccessAuthKey(sub);
        Map map = selectCache(key);
        String secret="";
        if (map == null || map.isEmpty()){
            AccessAuth accessAuth = accessAuthProvider.getAccessAuth(sub);
            if (accessAuth==null){
                return null;
            }
            secret = accessAuth.getPublic_key();
            //存储access_auth
            try{
                Map map1 = new HashMap();
                map1.put("public_key",accessAuth.getPublic_key());
                map1.put("private_key",accessAuth.getPrivate_key());
                map1.put("sub",accessAuth.getSub()+"");
                jedis.hmset(key,map1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            secret = map.get("public_key").toString();
        }
        return secret;
    }



}

