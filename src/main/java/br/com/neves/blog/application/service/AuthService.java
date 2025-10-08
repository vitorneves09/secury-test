package br.com.neves.blog.application.service;

import br.com.neves.blog.domain.entity.User;
import br.com.neves.blog.domain.repository.UserRepository;
import br.com.neves.blog.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    public String getAuthenticatedUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    public User getAuthenticatedUser() {
        String username = getAuthenticatedUsername();
        log.debug("Fetching authenticated user: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public Long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }
}
