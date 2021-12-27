package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.jwt.AccessToken;
import com.example.jwt.JwtUtils;
import com.example.security.mobile.MobileAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
//
//    @RequestMapping("/login")
//    public AccessToken login(String username, String password) {
//        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
//        Authentication authenticate = authenticationManager.authenticate(authentication);
//        SecurityContextHolder.getContext().setAuthentication(authenticate);//保存认证信息到上下文
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("username",username);
//        jsonObject.put("auth",authenticate.getAuthorities());
//        return new JwtUtils().createToken(jsonObject.toJSONString());
//    }
//
//    @RequestMapping("/test")
//    public String test(){
//        return "test";
//    }
//
//    @RequestMapping("/mobile/login")
//    public AccessToken mobileLogin(String mobile, String code) {
//        Authentication authentication = new MobileAuthenticationToken(mobile);
//        Authentication authenticate = authenticationManager.authenticate(authentication);
//        SecurityContextHolder.getContext().setAuthentication(authenticate);//保存认证信息到上下文
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("username",mobile);
//        jsonObject.put("auth",authenticate.getAuthorities());
//        return new JwtUtils().createToken(jsonObject.toJSONString());
//    }
}
