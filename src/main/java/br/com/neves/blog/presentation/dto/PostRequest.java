package br.com.neves.blog.presentation.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotBlank(message = "title is required")
    @Size(min = 10 , message = "Title most be more 10 characters ")
    private String title;

    @NotBlank(message = "content is required")
    @Size(min = 20 , message = "content most be more 10 characters")
    private String content;

    private String tag;

}
