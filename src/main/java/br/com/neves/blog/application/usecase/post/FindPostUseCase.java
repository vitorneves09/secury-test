package br.com.neves.blog.application.usecase.post;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import br.com.neves.blog.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindPostUseCase {

    private final PostRepository postRepository;

    public Post execute(Long id) {
        log.info("Finding post with id: {}", id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        log.info("Post found successfully with id: {}", id);

        return post;
    }
}
