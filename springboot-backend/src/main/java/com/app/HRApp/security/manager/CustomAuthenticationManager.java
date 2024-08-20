package com.app.HRApp.security.manager;

import javax.management.RuntimeErrorException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.HRApp.security.filter.AccountNotActivatedException;
import com.app.HRApp.service.UserService;
import com.app.HRApp.service.UserServiceImpl;
import com.app.HRApp.user.User;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private UserService userServiceImpl;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userServiceImpl.getUser(authentication.getName());
        if(!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())){
            throw new BadCredentialsException("Incorrect credentials.");
        }
        if(user.isEnabled() == false){
            throw new AccountNotActivatedException("Account not activated!");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword());
    }
    
}
