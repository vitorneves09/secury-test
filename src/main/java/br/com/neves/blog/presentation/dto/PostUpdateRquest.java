package br.com.neves.blog.presentation.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostUpdateRquest {

    @NotBlank(message = "title is required")
    @Size(min = 10 , message = "Title most be more 10 characters ")
    private String title;

    @Size(min = 0)
    private String content;

    private String tag;
}
