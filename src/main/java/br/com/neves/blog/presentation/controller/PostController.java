package br.com.neves.blog.presentation.controller;

import br.com.neves.blog.application.usecase.post.CreatePostUseCase;
import br.com.neves.blog.application.usecase.post.FindPostUseCase;
import br.com.neves.blog.application.usecase.post.ListAllPostUseCase;
import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.presentation.dto.CreatePostRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    private CreatePostUseCase createPostUseCase;

    @Autowired
    private FindPostUseCase findPostUseCase;

    @Autowired
    private ListAllPostUseCase listAllPostUseCase;

    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody CreatePostRequest request) {
        try {
            Post post = Post.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build();

            Post savedPost = createPostUseCase.execute(post);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Post> findPostById(@RequestParam Long id) {
        try {
            Post post = findPostUseCase.execute(id);

            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Post>> listAllPosts() {
        List<Post> posts = listAllPostUseCase.execute();
        return ResponseEntity.ok(posts);
    }

}
