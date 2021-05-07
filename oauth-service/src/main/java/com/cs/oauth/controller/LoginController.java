package com.cs.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @RequestMapping(value = "/loginAuth")
    public String loginAuth(){
        System.out.println("22222222");
        return "redirect:/index";
    }

    @RequestMapping(value = "/index")
    public String index(){
        System.out.println("登陆成功后跳转页面");
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/toerror")
    public String toerror(){
        System.out.println("登陆失败后跳转页面");
        return "redirect:/error.html";
    }
}
