package com.app.HRApp.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.HRApp.exception.ActivationCodeExpiredException;
import com.app.HRApp.exception.EmailAlreadyUsedException;
import com.app.HRApp.exception.EntityNotFoundException;
import com.app.HRApp.exception.ResetCodeExpiredException;
import com.app.HRApp.repository.PasswordResetTokenRepository;
import com.app.HRApp.repository.UserRepository;
import com.app.HRApp.user.PasswordResetToken;
import com.app.HRApp.user.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private EmailService emailService;

    @Override
    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return unwrapUser(user, id);
    }

    @Override
    public User saveUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())){
            throw new EmailAlreadyUsedException("Email already used!");
        }else{
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setToken(UUID.randomUUID().toString());
            emailService.sendSimpleMailMessage(user.getLastName(), user.getUsername(), user.getToken());
            user.setTimestamp(LocalDateTime.now());
            return userRepository.save(user);
        }
    }

    static User unwrapUser(Optional<User> entity, Long id) {
        if (entity.isPresent()) return entity.get();
        else throw new EntityNotFoundException(id, User.class);
    }

    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return unwrapUser(user, 404L);
    }

    @Override
    public User verifyToken(String token) {
        Optional<User> optionalUser = userRepository.findByToken(token);
        User user = optionalUser.get();
        LocalDateTime expirationTime = user.getTimestamp().plus(30 , ChronoUnit.MINUTES);
        if(user.getTimestamp().isAfter(expirationTime)){
            userRepository.delete(user);
            throw new ActivationCodeExpiredException("Activation Code Expired");
        }
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(String password, String username, String newPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        User finalUser = user.orElseThrow(() -> new RuntimeException("User not found"));
        if(bCryptPasswordEncoder.matches(password, finalUser.getPassword())){
            finalUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(finalUser);
            return finalUser;
        } else throw new RuntimeException("Inccorect password");
    }

    @Override
    public void sendResetPassword(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        User finalUser = user.orElseThrow(()-> new RuntimeException("User not found!"));
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordReset = new PasswordResetToken();

        passwordReset.setUsername(username);
        passwordReset.setToken(token);
        passwordReset.setCreationDate(LocalDateTime.now());

        passwordResetTokenRepository.save(passwordReset);

        emailService.sendPasswordChangeMail(finalUser.getFirstName(), username, token);    
    }

    @Override
    public void resetPassword(String newPassword , String token) {

        int attempt = 0;

        verifyResetToken(token);

        Optional<PasswordResetToken> tokenOption = passwordResetTokenRepository.findByToken(token);
        if (tokenOption.isEmpty()) {
            throw new NoSuchElementException("Invalid token");
        }
        PasswordResetToken finalToken = tokenOption.get();

        Optional<User> user = userRepository.findByUsername(finalToken.getUsername());
        User finalUser = user.orElseThrow(() -> new NoSuchElementException("No user found with email"));
        finalUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(finalUser);
        
        passwordResetTokenRepository.delete(finalToken);
    }

    @Override
    public void verifyResetToken(String token) {
       Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);
       PasswordResetToken finalToken = tokenOptional.orElseThrow(() -> new ResetCodeExpiredException("The reset link was already used once!"));

       if(finalToken.getCreationDate().plusMinutes(30).isBefore(LocalDateTime.now())) {
            throw new ResetCodeExpiredException("Reset link expired!");
        }
    }
    
}
