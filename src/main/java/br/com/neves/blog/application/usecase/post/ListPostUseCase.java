package br.com.neves.blog.application.usecase.post;


import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import br.com.neves.blog.presentation.dto.PostFilterDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class ListPostUseCase {

    private final PostRepository postRepository;

    public List<Post> execute() {
        log.info("Listing all posts");

        List<Post> posts = postRepository.findAll();

        log.info("Found {} posts", posts.size());

        return posts;
    }

    public List<Post> execute(PostFilterDTO filter) {
        log.info("Listing posts with filters: {}", filter);

        List<Post> posts = postRepository.findAll(filter);

        log.info("Found {} posts matching the filters", posts.size());

        return posts;
    }
}
