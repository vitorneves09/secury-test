package br.com.neves.blog.infrastructure.persistence.mapper;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.infrastructure.persistence.entity.PostEntity;


public class PostMapper {
    public static PostEntity toEntity(Post post) {
        return  PostEntity.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .tag(post.getTag())
                .slug(post.getSlug())
                .authorId(post.getAuthorId())
                .build();
    }

    public static Post toDomain(PostEntity entity) {
        return Post.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .tag(entity.getTag())
                .slug(entity.getSlug())
                .authorId(entity.getAuthorId())
                .build();
    }
}
