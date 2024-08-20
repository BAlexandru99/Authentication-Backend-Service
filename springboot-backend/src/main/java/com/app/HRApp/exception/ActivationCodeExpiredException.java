package com.app.HRApp.exception;

public class ActivationCodeExpiredException extends RuntimeException {
    public ActivationCodeExpiredException(String message){
        super(message);
    }
}
