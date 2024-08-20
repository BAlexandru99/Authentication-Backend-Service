package com.app.HRApp.mail;

public class MailUtils {
    
    public static String getEmailMessage(String name,String host,String token){
        return "Hello " + name +",\n\nYour new account has been create. Please click the link below to verify your account. \n\n" + 
        getVerificationUrl(host,token) + "\n\nThe HRStation support Team";
    }

    private static String getVerificationUrl(String host, String token) {
        return host + "user/activation?token=" + token;
    }
}
