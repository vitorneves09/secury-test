package br.com.neves.blog.infrastructure.persistence.repository;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.infrastructure.persistence.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostJpaRepository  extends JpaRepository<PostEntity, Long> {
    Optional<Post> findById(Long id);
    Optional<Post> findByTitle(String title);
    Optional<Post> findBySlug(String slug);
}
