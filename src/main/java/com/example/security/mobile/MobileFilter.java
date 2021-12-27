package com.example.security.mobile;

import com.example.common.exception.AuthenticationExceptionOutHandler;
import com.example.common.exception.AuthenticationFailHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sun.misc.Contended;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class MobileFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationExceptionOutHandler exceptionOutHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if ("/mobile/login".equals(requestURI)){
            String inputCode = request.getParameter("code");
            String validateCode="0000";
            try {
                validate(inputCode);
            }catch (AuthenticationException e){
                exceptionOutHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }
        doFilter(request,response,filterChain);
    }

    public void validate(String inputCode){
        String code="0000";
        if (!code.equals(inputCode)){
            throw new AuthenticationFailHandler("code error");
        }
        if (StringUtils.isBlank(inputCode)){
            throw new AuthenticationFailHandler("code is empty");
        }
    }
}
