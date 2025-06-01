package com.example.infrastructure.database.mappers;

import com.example.domain.User;
import com.example.domain.UserId;
import com.example.domain.Email;
import com.example.domain.Name;
import com.example.infrastructure.database.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserId userId = new UserId(entity.getId());
        Email email = new Email(entity.getEmail());
        Name name = new Name(entity.getFirstName(), entity.getLastName());

        return new User(userId, email, name, entity.getActive());
    }

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity(
                user.getId().getValue(),
                user.getEmail().getValue(),
                user.getName().getFirstName(),
                user.getName().getLastName()
        );

        entity.setActive(user.isActive());

        return entity;
    }

    public void updateEntityFromDomain(User user, UserEntity entity) {
        if (user == null || entity == null) {
            return;
        }

        entity.setEmail(user.getEmail().getValue());
        entity.setFirstName(user.getName().getFirstName());
        entity.setLastName(user.getName().getLastName());
        entity.setActive(user.isActive());
    }
}