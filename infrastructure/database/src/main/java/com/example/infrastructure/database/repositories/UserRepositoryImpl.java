package com.example.infrastructure.database.repositories;

import com.example.application.ports.UserRepository;
import com.example.domain.User;
import com.example.domain.UserId;
import com.example.domain.Email;
import com.example.infrastructure.database.entities.UserEntity;
import com.example.infrastructure.database.mappers.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository, UserMapper userMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaUserRepository.findById(id.getValue())
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaUserRepository.findByEmail(email.getValue())
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findActiveUsers() {
        return jpaUserRepository.findByActiveTrue()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(UserId id) {
        jpaUserRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(UserId id) {
        return jpaUserRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaUserRepository.existsByEmail(email.getValue());
    }

    @Override
    public long count() {
        return jpaUserRepository.count();
    }

    @Override
    public long countActiveUsers() {
        return jpaUserRepository.countActiveUsers();
    }

    @Override
    public List<User> findByNameContaining(String name) {
        return jpaUserRepository.findByNameContaining(name)
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
}