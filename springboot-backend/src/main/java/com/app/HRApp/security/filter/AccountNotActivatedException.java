package com.app.HRApp.security.filter;

import org.springframework.security.core.AuthenticationException;

public class AccountNotActivatedException extends AuthenticationException {
    public AccountNotActivatedException(String msg) {
        super(msg);
    }
}
