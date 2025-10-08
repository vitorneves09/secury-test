package br.com.neves.blog.presentation.controller;

import br.com.neves.blog.application.usecase.CreateUserUseCase;
import br.com.neves.blog.application.usecase.FindUserUseCase;
import br.com.neves.blog.domain.entity.User;
import br.com.neves.blog.presentation.dto.CreateUserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private FindUserUseCase findUserUseCase;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            User user = User.builder()
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .email(request.getEmail())
                    .role(request.getRole())
                    .build();

            User createdUser = createUserUseCase.execute(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = findUserUseCase.findById(id);
        return user.map(u -> ResponseEntity.ok().body(u))
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = findUserUseCase.findByEmail(email);
        return user.map(u -> ResponseEntity.ok().body(u))
                   .orElse(ResponseEntity.notFound().build());
    }
}
