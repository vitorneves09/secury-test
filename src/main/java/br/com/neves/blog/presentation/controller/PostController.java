package br.com.neves.blog.presentation.controller;

import br.com.neves.blog.application.usecase.post.CreatePostUseCase;
import br.com.neves.blog.application.usecase.post.FindPostUseCase;
import br.com.neves.blog.application.usecase.post.ListAllPostUseCase;
import br.com.neves.blog.application.usecase.post.UpdatePostUseCase;
import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.infrastructure.persistence.specification.PostSpecification;
import br.com.neves.blog.presentation.dto.PostFilterDTO;
import br.com.neves.blog.presentation.dto.PostRequest;
import br.com.neves.blog.presentation.dto.PostResponse;
import br.com.neves.blog.presentation.dto.PostUpdateRquest;
import br.com.neves.blog.presentation.mapper.PostDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {

    private final CreatePostUseCase createPostUseCase;
    private final FindPostUseCase findPostUseCase;
    private final ListAllPostUseCase listAllPostUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    private final PostDtoMapper postDtoMapper;

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest request) {
       try{
           Post post = postDtoMapper.toDomain(request);

           Post savedPost = createPostUseCase.execute(post);

           return ResponseEntity.status(HttpStatus.CREATED).body(postDtoMapper.toResponse(savedPost));
       }catch (Exception ex){
           HashMap<String, Object> response = new HashMap<>();
           response.put("message", ex.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
    public ResponseEntity<List<Post>> listAllPosts(PostFilterDTO filterDTO) {
        List<Post> posts = listAllPostUseCase.execute(filterDTO);

        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRquest request)  {
        Post post = postDtoMapper.toDomain(request);

        Post updatedPost = updatePostUseCase.execute(id, post);

        return ResponseEntity.ok(postDtoMapper.toResponse(updatedPost));
    }

}
