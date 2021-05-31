package com.cs.consulApp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.cs.api.pojo.AccessAuth;
import com.cs.api.pojo.User;
import com.cs.api.provider.IAccessAuthProvider;
import com.cs.api.provider.IAccountProvider;
import com.cs.common.*;
import com.cs.common.encryption.RSAEncrypt;
import com.cs.common.json.JsonUtils;
import com.cs.consulApp.feign.AuthFeign;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    @Reference
    IAccountProvider accountProvider;

    @Reference
    IAccessAuthProvider accessAuthProvider;

    @Autowired
    AuthFeign authFeign;




    @RequestMapping(value = "/adminLogin")
    public String adminLogin(String username,String password){

        if (WebUtil.checkEmpty(username,password)){
            return Result.error(CodeMsg.PARAMETER_ISNULL);
        }
        String password1 = MD5Util.getMd5(password);
        User user = accountProvider.login(username, password1);
        if (user==null){
            return Result.error(CodeMsg.LOGIN_FAILD);
        }
        //获取token
        String authMsg = authFeign.getToken(user.getId().toString(),"www.consul.com");
        JSONObject jo = JsonUtils.parseObject(authMsg);
        Map map = new HashMap();
        map.put("uid",user.getId());
        map.put("name",user.getNickName());
        if(jo.getIntValue("retCode")!=1){
            return authMsg;
        }
        map.put("accessToken", jo.getJSONObject("data"));
        return Result.ok(map);
    }

    @RequestMapping(value = "/authorization")
    public String authorization(String sub,String aud) throws NoSuchAlgorithmException {

        if (WebUtil.checkEmpty(sub,aud)){
            return Result.error(CodeMsg.PARAMETER_ISNULL);
        }

        AccessAuth accessAuth = new AccessAuth();
        accessAuth.setSub(Long.valueOf(sub));
        accessAuth.setAccessAuth("www.consul.com");
        accessAuth.setType("all");

        RSAEncrypt rsaEncrypt = new RSAEncrypt();
        rsaEncrypt.genKeyPair();
        accessAuth.setPublic_key(rsaEncrypt.public_key);
        accessAuth.setPrivate_key(rsaEncrypt.private_key);
        accessAuth.setAddTime(new Date());
        accessAuthProvider.registerAccess(accessAuth);
        return Result.ok(accessAuth);
    }

    @RequestMapping(value = "/userList")
    public String userList(String page, String name){
        int currentPage = (page!=null && !page.equals(""))?Integer.valueOf(page):1;
        Map query = new HashMap();
        if (!CommUtil.null2String(name).equals("")){
            query.put("name",name);
        }
        PageBean pageBean = accountProvider.getList(currentPage,20,query);
        List<User>  users= pageBean.getList() ;
        Map map= new HashMap<>();
        map.put("totalPage",pageBean.getTotalPage());
        map.put("hasNext",pageBean.isHasNextPage());
        map.put("firstPage",pageBean.isFirstPage());
        map.put("hasPre",pageBean.isHasPreviousPage());
        map.put("rows",pageBean.getAllRow());
        map.put("currentPage",pageBean.getCurrentPage());
        map.put("lastPage",pageBean.isLastPage());
        map.put("records",users);
        return Result.ok(map);
    }

    @RequestMapping(value = "/deluser")
    public String deluser(String id){
        accountProvider.unableUser(Long.valueOf(id));
        return Result.ok();
    }

    @RequestMapping(value = "/updateUser")
    public Result updateUser(String id,User user){
        User user1 = accountProvider.findUser(Long.valueOf(id));
        BeanUtils.copyProperties(user,user1);
        accountProvider.update(user1);
        return Result.success(user);
    }


    @RequestMapping(value = "/userInfo")
    public Result userInfo(String id){
        User user = accountProvider.findUser(Long.valueOf(id));
        return Result.success(user);
    }

    @RequestMapping("/roleList")
    public Result roleList(String uid){
        User user = accountProvider.findUser(Long.valueOf(uid));
        return Result.success(user);
    }



    /*@RequestMapping(value = "/addUser")
    public String addUser(String name,String password){

        *//*if (WebUtil.checkEmpty(username,password)){
            return Result.error(CodeMsg.PARAMETER_ISNULL);
        }*//*

        return Result.success();
    }

    @RequestMapping(value = "/quit_admin")
    public String quitadmin(String uid){

        *//*if (WebUtil.checkEmpty(username,password)){
            return Result.error(CodeMsg.PARAMETER_ISNULL);
        }*//*

        return Result.success();
    }*/

}
