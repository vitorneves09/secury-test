package br.com.neves.blog.application.usecase.post;

import br.com.neves.blog.application.service.AuthService;
import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.entity.User;
import br.com.neves.blog.domain.repository.PostRepository;
import br.com.neves.blog.exception.NotPermissionException;
import br.com.neves.blog.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePostUseCase {

    private final PostRepository postRepository;
    private final AuthService authService;

    public Post execute(Long postId, Post updatedPost) {
        log.info("Updating post with id: {}", postId);

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        User author = authService.getAuthenticatedUser();

        if (!author.canEditPost(existingPost)) {
            log.warn("User {} tried to edit post {} without permission", author.getUsername(), postId);
            throw new NotPermissionException("post", "edit");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setTag(updatedPost.getTag());

        Post savedPost = postRepository.save(existingPost);

        log.info("Post {} updated successfully", postId);

        return savedPost;
    }
}
