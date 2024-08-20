package com.app.HRApp.exception;

public class EmailAlreadyUsedException extends RuntimeException {
    
    public EmailAlreadyUsedException(String message){
        super(message);
    }
}
