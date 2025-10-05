package br.com.neves.blog.application.usecase;

import br.com.neves.blog.domain.entity.User;
import br.com.neves.blog.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindUserUseCase {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        throw new UnsupportedOperationException("findById ainda não implementado");
    }
}
