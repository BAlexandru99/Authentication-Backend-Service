package com.app.HRApp.service;

public interface EmailService {
    void sendSimpleMailMessage(String name , String to, String token);
    void sendPasswordChangeMail(String name , String to , String token);
}
