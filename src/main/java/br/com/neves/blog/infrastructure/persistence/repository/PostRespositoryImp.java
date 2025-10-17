package br.com.neves.blog.infrastructure.persistence.repository;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import br.com.neves.blog.infrastructure.persistence.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PostRespositoryImp implements PostRepository {

    @Autowired
    PostJpaRepository repository;

    public Optional<Post> findById(Long id) {
        return repository.findById(id).map(PostMapper::toDomain);
    }

    public Optional<Post> findByTitle(String title) {
        return repository.findByTitle(title).map(PostMapper::toDomain);
    }

    public Optional<Post> findBySlug(String slug) {
        return repository.findBySlug(slug).map(PostMapper::toDomain);
    }

    public boolean existsBySlug(String slug) { return repository.existsBySlug(slug); }

    public boolean existsByTitle(String title) { return repository.existsByTitle(title); }

    @Override
    public Post save(Post post) {
        return PostMapper.toDomain(repository.save(PostMapper.toEntity(post)));
    }

    @Override
    public List<Post> findAll() {
        return repository.findAll().stream()
                .map(PostMapper::toDomain)
                .collect(Collectors.toList());
    }
}
