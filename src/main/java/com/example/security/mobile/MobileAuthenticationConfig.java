package com.example.security.mobile;

import com.example.security.SecUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 用于组合其他关于手机登录的组件
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@Component
public class MobileAuthenticationConfig
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    @Autowired
    SecUserService secUserService;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter();
        // 获取容器中已经存在的AuthenticationManager对象，并传入 mobileAuthenticationFilter 里面
        mobileAuthenticationFilter.setAuthenticationManager(
                http.getSharedObject(AuthenticationManager.class));


        // 构建一个MobileAuthenticationProvider实例，接收 mobileUserDetailsService 通过手机号查询用户信息
        MobileAuthenticationProvider provider = new MobileAuthenticationProvider(secUserService);

        // 将provider绑定到 HttpSecurity上，并将 手机号认证过滤器绑定到用户名密码认证过滤器之后
        http.authenticationProvider(provider)
                .addFilterAfter(mobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
