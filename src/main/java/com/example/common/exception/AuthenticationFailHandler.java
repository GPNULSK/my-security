package com.example.common.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailHandler extends AuthenticationException {
    public AuthenticationFailHandler(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationFailHandler(String msg) {
        super(msg);
    }
}
