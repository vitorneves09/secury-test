package br.com.neves.blog.application.service;


import br.com.neves.blog.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest
public class AuthServiceTest {


    @Autowired
    private AuthServiceImp authService;

    @MockitoBean
    private UserRepository userRepository;


    public void shouldCreateUserWithSucess() {

    }
}
