package br.com.neves.blog.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class Post {
    Long id;
    String title;
    String content;
    String tag;
    Date created;
    Long authorId;
    String slug;
}
