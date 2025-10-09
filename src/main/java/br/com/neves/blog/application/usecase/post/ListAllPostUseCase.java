package br.com.neves.blog.application.usecase.post;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListAllPostUseCase {

    private final PostRepository postRepository;

    public List<Post> execute() {
        log.info("Listing all posts");

        List<Post> posts = postRepository.findAll();

        log.info("Found {} posts", posts.size());

        return posts;
    }
}
