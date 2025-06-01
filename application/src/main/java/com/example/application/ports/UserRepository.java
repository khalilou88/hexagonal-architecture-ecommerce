package com.example.application.ports;

import com.example.domain.User;
import com.example.domain.UserId;
import com.example.domain.Email;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    List<User> findAll();
    List<User> findActiveUsers();
    User save(User user);
    void delete(UserId id);
    boolean existsById(UserId id);
    boolean existsByEmail(Email email);
    long count();
    long countActiveUsers();
    List<User> findByNameContaining(String name);
}