package br.com.neves.blog.application.usecase.post;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import br.com.neves.blog.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindPostUseCase {

    @Autowired
    private PostRepository postRepository;

    public Post execute(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

}
