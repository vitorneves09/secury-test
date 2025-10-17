package br.com.neves.blog.application.usecase.post;

import br.com.neves.blog.application.service.AuthService;
import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatePostUseCase {

    private final PostRepository postRepository;
    private final AuthService authService;

    public Post execute(Post post) {

        isValid(post);

        String username = authService.getAuthenticatedUsername();

        log.info("Creating post for user: {}", username);

        Long userId = authService.getAuthenticatedUserId();

        post.setAuthorId(userId);
        post.setCreated(new Date());

        Post savedPost = postRepository.save(post);

        log.info("Post created successfully with id: {}", savedPost.getId());

        return savedPost;
    }

    public void isValid(Post post) {

        if(!post.getTitle().isBlank() && postRepository.findByTitle(post.getTitle()).isPresent()) {
            throw new ValidationException("Title already exists");
        }

    }
}
