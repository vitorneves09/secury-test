package br.com.neves.blog.domain.repository;

import br.com.neves.blog.domain.entity.Post;

import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long id);
    Optional<Post> findByTitle(String title);
    Optional<Post> findBySlug(String slug);
}
