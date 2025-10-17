package br.com.neves.blog.presentation.controller;

import br.com.neves.blog.application.usecase.user.AuthenticateUserUseCase;
import br.com.neves.blog.presentation.dto.LoginRequest;
import br.com.neves.blog.presentation.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthenticateUserUseCase authenticateUserUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
       try {
           LoginResponse response = authenticateUserUseCase.execute(loginRequest);

           return ResponseEntity.ok(response);
       }catch (Exception e) {
           HashMap<String, String> error = new HashMap<>();
           error.put("error", e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
       }
    }
}
