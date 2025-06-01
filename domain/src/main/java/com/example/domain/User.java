package com.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private final UserId id;
    private Email email;
    private Name name;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor for new users
    public User(UserId id, Email email, Name name) {
        this.id = Objects.requireNonNull(id, "User ID cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor for existing users (from repository)
    public User(UserId id, Email email, Name name, boolean active) {
        this.id = Objects.requireNonNull(id, "User ID cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.active = active;
        this.createdAt = LocalDateTime.now(); // This would come from DB in real scenario
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public void updateProfile(Name newName, Email newEmail) {
        validateProfileUpdate(newName, newEmail);
        this.name = newName;
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canBeDeleted() {
        // Business rule: only inactive users can be deleted
        return !this.active;
    }

    private void validateProfileUpdate(Name newName, Email newEmail) {
        if (newName == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (newEmail == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        // Additional business validation can be added here
    }

    // Getters
    public UserId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Name getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", name=" + name +
                ", active=" + active +
                '}';
    }
}