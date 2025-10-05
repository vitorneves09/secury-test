package br.com.neves.blog.application.usecase.post;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAllPostUseCase {

    @Autowired
    private PostRepository postRepository;

    public List<Post> execute() {
        return postRepository.findAll();
    }
}
