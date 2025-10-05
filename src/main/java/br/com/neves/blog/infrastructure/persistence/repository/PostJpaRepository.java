package br.com.neves.blog.infrastructure.persistence.repository;

import br.com.neves.blog.infrastructure.persistence.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostJpaRepository  extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findById(Long id);
    Optional<PostEntity> findByTitle(String title);
    Optional<PostEntity> findBySlug(String slug);
}
