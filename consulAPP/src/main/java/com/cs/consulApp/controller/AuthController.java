package com.cs.consulApp.controller;

import com.alibaba.fastjson.JSON;
import com.cs.api.pojo.User;
import com.cs.api.provider.IAccountProvider;
import com.cs.common.*;
import com.google.gson.Gson;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    @Reference
    IAccountProvider accountProvider;

    @RequestMapping(value = "/adminLogin")
    public Result adminLogin(String username,String password){

        if (WebUtil.checkEmpty(username,password)){
            return Result.error(CodeMsg.PARAMETER_ISNULL);
        }

        User user = accountProvider.login(username, password);
        if (user!=null){
            return Result.error(CodeMsg.LOGIN_FAILD);
        }
        return Result.success();
    }

    @RequestMapping(value = "/userList")
    public Result userList(String page,String name){
        int currentPage = (page!=null && !page.equals(""))?Integer.valueOf(page):1;
        Map query = new HashMap();
        if (!CommUtil.null2String(name).equals("")){
            query.put("name",name);
        }
        PageBean pageBean = accountProvider.getList(currentPage,20,query);
        List<User>  users=  pageBean.getList();
        for (User user:users) {


        }
        return Result.success(pageBean);
    }

    @RequestMapping(value = "/addUser")
    public Result addUser(String name,String password){

        /*if (WebUtil.checkEmpty(username,password)){
            return Result.error(CodeMsg.PARAMETER_ISNULL);
        }*/

        return Result.success();
    }

}
