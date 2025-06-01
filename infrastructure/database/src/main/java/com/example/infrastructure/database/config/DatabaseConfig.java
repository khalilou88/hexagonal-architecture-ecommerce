package com.example.infrastructure.database.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.infrastructure.database.repositories")
@EntityScan(basePackages = "com.example.infrastructure.database.entities")
@EnableTransactionManagement
public class DatabaseConfig {
    // Additional database configuration can be added here if needed
}
