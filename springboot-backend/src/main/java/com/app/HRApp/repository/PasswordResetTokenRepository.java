package com.app.HRApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.CrudMethods;

import com.app.HRApp.user.PasswordResetToken;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUsername(String username);
    void deleteByToken(String token);
}
