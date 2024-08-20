package com.app.HRApp.security;

public class SecurityConstants {
    public static final String SECRET_KEY = "bQeQQFaWmZQg27w#!?z$C&F)]{J@NcRfUjXn2r5u8x/A?DI%H7Aa;-KaPdSgVkYp3s6v9y$B&E)"; //Your secret should always be strong (uppercase, lowercase, numbers, symbols) so that nobody can potentially decode the signature.
    public static final int TOKEN_EXPIRATION = 7200000; // 7200000 milliseconds = 7200 seconds = 2 hours.
    public static final String BEARER = "Bearer "; // Authorization : "Bearer " + Token 
    public static final String AUTHORIZATION = "Authorization"; // "Authorization" : Bearer Token
    public static final String REGISTER_PATH = "/user/register"; // Public path that clients can use to register.
    public static final String ACTIVATION_PATH = "/user/activation";
    public static final String RESET_PATH = "/user/reset";
    public static final String RESET_PATH_PASSWORD = "/user/reset-password";
}
