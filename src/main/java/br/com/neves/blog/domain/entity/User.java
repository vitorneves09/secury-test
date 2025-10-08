package br.com.neves.blog.domain.entity;

import br.com.neves.blog.domain.enums.RoleEnum;
import lombok.Builder;
import lombok.Data;

import javax.management.relation.Role;

@Data
@Builder
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;

    public boolean canEditPost(Post post) {
        if(!this.role.isBlank() && this.role.equals(String.valueOf(RoleEnum.ADMIN))){
            return true;
        }

        return post.authorId.equals(this.id);
    }
}
