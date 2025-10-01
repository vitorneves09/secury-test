package br.com.neves.blog.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
}
