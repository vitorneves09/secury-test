package br.com.neves.blog.infrastructure.persistence.mapper;

import br.com.neves.blog.domain.entity.User;
import br.com.neves.blog.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public static User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }
}
