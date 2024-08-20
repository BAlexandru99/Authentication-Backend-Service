package com.app.HRApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import com.app.HRApp.security.filter.AuthenticationFilter;
import com.app.HRApp.security.filter.ExceptionHandlerFilter;
import com.app.HRApp.security.filter.JWTAuthorizationFilter;
import com.app.HRApp.security.manager.CustomAuthenticationManager;

import lombok.AllArgsConstructor;


@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig  {

    CustomAuthenticationManager customAuthenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager);
        authenticationFilter.setFilterProcessesUrl("/authenticate");
        http        
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST , SecurityConstants.REGISTER_PATH).permitAll()
            .antMatchers(HttpMethod.GET, SecurityConstants.ACTIVATION_PATH).permitAll()
            .antMatchers(HttpMethod.POST, SecurityConstants.RESET_PATH).permitAll()
            .antMatchers(HttpMethod.GET, SecurityConstants.RESET_PATH).permitAll()
            .antMatchers(HttpMethod.POST, SecurityConstants.RESET_PATH_PASSWORD).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
            .addFilter(authenticationFilter)
            .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}

