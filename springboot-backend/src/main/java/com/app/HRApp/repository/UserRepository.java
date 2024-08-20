package com.app.HRApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.app.HRApp.user.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
   Optional <User> findByUsername(String username);
   boolean existsByUsername(String username);
   Optional<User> findByToken(String token);
}
