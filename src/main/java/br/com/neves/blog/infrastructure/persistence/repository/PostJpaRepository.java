package br.com.neves.blog.infrastructure.persistence.repository;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.infrastructure.persistence.entity.PostEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository  extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {
    Optional<PostEntity> findById(Long id);
    Optional<PostEntity> findByTitle(String title);
    Optional<PostEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsByTitle(String title);
}
