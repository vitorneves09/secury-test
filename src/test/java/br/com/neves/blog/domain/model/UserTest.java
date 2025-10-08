package br.com.neves.blog.domain.model;

import br.com.neves.blog.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Test
    public void shouldBeAbleToCreateUser() {
        User user = User.builder()
                .username("test")
                .password("password")
                .email("teste@teste.com")
                .build();

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getUsername()).isEqualTo("test");
        Assertions.assertThat(user.getPassword()).isEqualTo("password");
    }
}
