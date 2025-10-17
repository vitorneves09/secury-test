package br.com.neves.blog.domain.repository;

import br.com.neves.blog.domain.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long id);
    Optional<Post> findByTitle(String title);
    Optional<Post> findBySlug(String slug);
    Post save(Post post);
    List<Post> findAll();
    boolean existsBySlug(String slug);
    boolean existsByTitle(String title);
}
