package br.com.neves.blog.presentation.mapper;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.presentation.dto.PostRequest;
import br.com.neves.blog.presentation.dto.PostResponse;
import br.com.neves.blog.presentation.dto.PostUpdateRquest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostDtoMapper {

    public Post toDomain(PostRequest request) {
        return Post.builder()
                .title(sanitize(request.getTitle()))
                .content(sanitize(request.getContent()))
                .tag(sanitize(request.getTag()))
                .build();
    }

    public Post toDomain(PostUpdateRquest request) {
        return Post.builder()
                .title(sanitize(request.getTitle()))
                .content(sanitize(request.getContent()))
                .tag(sanitize(request.getTag()))
                .build();
    }

    public PostResponse toResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .tag(post.getTag())
                .created(post.getCreated())
                .authorId(post.getAuthorId())
                .slug(post.getSlug())
                .build();
    }

    public PostResponse toResponse(Post post, String authorName) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .tag(post.getTag())
                .created(post.getCreated())
                .authorId(post.getAuthorId())
                .authorName(authorName)
                .slug(post.getSlug())
                .build();
    }

    private String sanitize(String value) {
        return Optional.ofNullable(value)
                .map(String::trim)
                .filter(v -> !v.isBlank())
                .orElse(null);
    }
}
