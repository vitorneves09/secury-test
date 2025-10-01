package br.com.neves.blog.domain.repository;

import br.com.neves.blog.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User save(User user);
}
