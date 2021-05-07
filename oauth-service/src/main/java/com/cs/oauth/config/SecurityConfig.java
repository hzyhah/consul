package com.cs.oauth.config;

import com.cs.oauth.handle.MyAccess;
import com.cs.oauth.handle.MyAuthenticationFailureHandler;
import com.cs.oauth.handle.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyAccess myAccess;


    //可重写密码加密实现方式
    @Bean
    public PasswordEncoder getPwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.formLogin()
                //自定义登陆表单提交验证参数
                .usernameParameter("name")
                .passwordParameter("password")
                //登陆页面提交请求路径设置放行，必须和登陆表单action路径一致，并且去执行
                //UserDetailsServiceImpl下loadUserByUsername方法
                *//*.loginProcessingUrl("/loginAuth")*//*
                .loginProcessingUrl("/login")
                //指定自定义登陆页面
                .loginPage("/login.html")
                //登陆成功后跳转页面，登陆成功后，跳转页面必须是一个post请求
                .successForwardUrl("/index")
                //登陆失败，post跳转
                .failureForwardUrl("/toerror");*/


                /*//登陆成功处理器，不能和successForwardUrl共存，适用于站外跳转，前后端分离
                .successHandler(new MyAuthenticationSuccessHandler("https://baidu.com"))
                //登陆失败处理器，不能和failureForwardUrl共存，适用于站外跳转，前后端分离
                .failureHandler(new MyAuthenticationFailureHandler("http://163.com"));*/

        http.authorizeRequests()
                //不进行授权验证页面
                .antMatchers("/oauth/**","/loginAuth","/login.html","/error.html**","/toerror**").permitAll()
                //所有请求都必须进行授权认证
                .anyRequest().authenticated()
                .and().formLogin().permitAll();

        //关闭一个防火墙
        http.csrf().disable();

        http.exceptionHandling()
                .accessDeniedHandler(myAccess);

    }
}
