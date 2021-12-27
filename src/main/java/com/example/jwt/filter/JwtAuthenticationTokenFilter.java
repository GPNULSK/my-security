package com.example.jwt.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.jwt.JwtProperties;
import com.example.jwt.JwtUtils;
import com.example.security.UserEntity;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * JWT登录过滤器
 * </p>
 * <p>
 * 拿到请求头中的token解析出其中的用户信息，
 * 将用户信息传给下一条过滤器，
 * 拿到上下文对象赋值到上下文。
 * <p>
 *
 * @author 和耳朵
 * @since 2020-06-30
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private JwtProperties jwtProperties;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {


        // 拿到Authorization请求头内的信息
        String authToken = jwtUtils.getToken(request);
        log.info("JWT信息为："+authToken);
        // 判断一下内容是否为空
        if (StrUtil.isNotEmpty(authToken) && authToken.startsWith(jwtProperties.getTokenPrefix())) {
            // 去掉token前缀(Bearer )，拿到真实token
            authToken = authToken.substring(jwtProperties.getTokenPrefix().length());

            // 拿到token里面的登录账号
            String jwtToken = jwtUtils.getSubjectFromToken(authToken);

            Authentication curAuthentication = SecurityContextHolder.getContext().getAuthentication();
            if (curAuthentication != null) {
                String username = (String) curAuthentication.getPrincipal();
                boolean validateToken = jwtUtils.validateToken(authToken, username);
                if (validateToken) {
                    chain.doFilter(request, response);
                }
            } else {
                JSONObject jsonObject = JSONObject.parseObject(jwtToken);
                String username = (String) jsonObject.get("username");
                JSONArray auth = (JSONArray) jsonObject.get("auth");
                Map<String,Object> authArray = (Map<String, Object>) auth.get(0);
                ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                String auth1 = (String) authArray.get("authority");
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(auth1);
                grantedAuthorities.add(simpleGrantedAuthority);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
