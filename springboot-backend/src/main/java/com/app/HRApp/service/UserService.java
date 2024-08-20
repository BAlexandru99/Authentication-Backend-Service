package com.app.HRApp.service;

import com.app.HRApp.user.User;

public interface UserService {
    User getUser(Long id);
    User getUser(String username);
    User saveUser(User user);
    User verifyToken(String token);
    User updatePassword(String password, String username, String newPassword);
    void sendResetPassword(String username);
    void resetPassword(String newPassword , String token);
    void verifyResetToken(String token);
}
