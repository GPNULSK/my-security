package com.example.security.mobile;


import com.example.security.SecUserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class MobileAuthenticationProvider implements AuthenticationProvider {

    private SecUserService secUserService;

    //使用构造方法注入SecUserService
    public MobileAuthenticationProvider(SecUserService secUserService) {
        this.secUserService = secUserService;
    }

    //认证处理，验证码已经在过滤器通过，假如查询到有手机号则有用户信息，并且登录成功，封装用户信息
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
        String mobile = (String) mobileAuthenticationToken.getCredentials();
        UserDetails userDetails = secUserService.loadUserByUsername(mobile);
        if (userDetails == null) {
            throw new AuthenticationServiceException("手机号为注册");
        }

        MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
        //把传过来的authentication里的details放到自己的Authentication中
        authenticationToken.setDetails(authentication.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return MobileAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
