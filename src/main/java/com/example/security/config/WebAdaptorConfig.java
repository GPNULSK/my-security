package com.example.security.config;

import com.example.MySecurityApplication;
import com.example.jwt.filter.JwtAuthenticationTokenFilter;
import com.example.security.LoginFailureHandler;
import com.example.security.LoginSuccessHandler;
import com.example.security.MyAuthenticationEntryPoint;
import com.example.security.MyUserDetailsService;
import com.example.security.mobile.MobileAuthenticationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebAdaptorConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private MyAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private MobileAuthenticationConfig mobileAuthenticationConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers("/login","mobile/login").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                // 异常处理(权限拒绝、登录失效等)
                .and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)//匿名用户访问无权限资源时的异常处理

                // 登入
                .and().formLogin().permitAll()//允许所有用户
                .successHandler(loginSuccessHandler)//登录成功处理逻辑
                .failureHandler(loginFailureHandler)//登录失败处理逻辑
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        http.apply(mobileAuthenticationConfig);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
