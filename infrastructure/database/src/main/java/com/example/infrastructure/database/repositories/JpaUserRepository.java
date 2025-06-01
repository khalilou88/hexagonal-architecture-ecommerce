package com.example.infrastructure.database.repositories;

import com.example.infrastructure.database.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByActiveTrue();

    @Query("SELECT u FROM UserEntity u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    List<UserEntity> findByNameContaining(@Param("name") String name);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.active = true")
    long countActiveUsers();
}