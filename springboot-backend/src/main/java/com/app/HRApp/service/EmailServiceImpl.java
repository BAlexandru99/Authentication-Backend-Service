package com.app.HRApp.service;



import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.management.RuntimeErrorException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.app.HRApp.mail.MailUtils;

import lombok.AllArgsConstructor;

@Service
public class EmailServiceImpl implements EmailService {


    private String host = "http://localhost:8080/";
    private final JavaMailSender emailSender;
        private final SpringTemplateEngine templateEngine;


    public EmailServiceImpl(JavaMailSender emailSender , SpringTemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

        @Override
        @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            Context context = new Context();
            context.setVariable("username", name);
            context.setVariable("activation_code", token);
            context.setVariable("confirmationUrl", host + "user/activation?token=" + token);

            String htmlContent = templateEngine.process("activation-email", context);
            
            helper.setTo(to);
            helper.setSubject("Account Verification");
            helper.setFrom("noreply@hr-station.com");
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

        @Override
        @Async
        public void sendPasswordChangeMail(String name, String to, String token) {
            try{
                MimeMessage message = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                Context context = new Context();
                context.setVariable("username", name);
                context.setVariable("activation_code", token);
                context.setVariable("confirmationUrl", host + "user/reset?token=" + token);

                String htmlContent = templateEngine.process("reset-email", context);

                helper.setTo(to);
                helper.setSubject("Reset Password");
                helper.setFrom("noreply@hr-station.com");
                helper.setText(htmlContent, true);

                emailSender.send(message);
            }catch(MessagingException e){
                throw new RuntimeException(e.getMessage());
            }
        }
}


