package br.com.neves.blog.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String title;

    @Column()
    String content;

    @Column
    String tag;

    @Column(name = "created_at")
    Date created;

    @Column(name = "updated_at")
    Date updated;

    @Column(unique = true)
    String slug;
}
