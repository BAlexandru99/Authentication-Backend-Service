package com.app.HRApp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.Email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.app.HRApp.exception.ResetCodeExpiredException;
import com.app.HRApp.repository.PasswordResetTokenRepository;
import com.app.HRApp.repository.UserRepository;
import com.app.HRApp.service.EmailService;
import com.app.HRApp.service.UserService;
import com.app.HRApp.service.UserServiceImpl;
import com.app.HRApp.user.PasswordResetToken;
import com.app.HRApp.user.User;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void saveUserFromRepoTest(){
        //Test Date
        User user = new User(0, "testuser", "password", "FirstName", "LastName", 123456789L, null, null, false);

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
    
        User savedUser = userService.saveUser(user);

        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(emailService).sendSimpleMailMessage(user.getLastName(), user.getUsername(), user.getToken());
        verify(userRepository).save(user);
    }

    @Test
    public void testSavePasswordResetToken() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUsername("testuser@mail.com");
        token.setCreationDate(LocalDateTime.now());

        PasswordResetToken savedToken = passwordResetTokenRepository.save(token);

        assertNotNull(savedToken.getId());
    }

    @Test
    public void testExpirationActivation() {
            // Setup test data
            String token = UUID.randomUUID().toString();
            LocalDateTime creationTime = LocalDateTime.now().minusMinutes(2); // token created 2 minutes ago
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(token);
            passwordResetToken.setCreationDate(creationTime);
    
            // Mock repository behavior
            when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(passwordResetToken));
    
            // Verify that ResetCodeExpiredException is thrown for expired token
            assertThrows(ResetCodeExpiredException.class, () -> userService.verifyResetToken(token));
        }
    }



