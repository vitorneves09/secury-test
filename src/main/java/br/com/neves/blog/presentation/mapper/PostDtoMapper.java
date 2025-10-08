package br.com.neves.blog.presentation.mapper;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.presentation.dto.PostRequest;
import br.com.neves.blog.presentation.dto.PostResponse;
import org.springframework.stereotype.Component;

@Component
public class PostDtoMapper {

    public Post toDomain(PostRequest request) {
        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .tag(request.getTag())
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
}
