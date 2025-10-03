package br.com.neves.blog.infrastructure.persistence.repository;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PostRespositoryImp implements PostRepository {

    @Autowired
    PostJpaRepository repository;

    public Optional<Post> findById(Long id) {
        return repository.findById(id).map(Post);
    }

    public Optional<Post> findByTitle(String title) {
        return Optional.empty();
    }

    public Optional<Post> findBySlug(String slug) {
        return Optional.empty();
    }
}
