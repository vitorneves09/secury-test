package br.com.neves.blog.infrastructure.persistence.specification;

import br.com.neves.blog.infrastructure.persistence.entity.PostEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PostSpecification {

    public static Specification<PostEntity> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("title")),
                "%" + title.toLowerCase() + "%"
            );
        };
    }

    public static Specification<PostEntity> hasContent(String content) {
        return (root, query, criteriaBuilder) -> {
            if (content == null || content.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("content")),
                "%" + content.toLowerCase() + "%"
            );
        };
    }

    public static Specification<PostEntity> hasTag(String tag) {
        return (root, query, criteriaBuilder) -> {
            if (tag == null || tag.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("tag")),
                "%" + tag.toLowerCase() + "%"
            );
        };
    }

    public static Specification<PostEntity> hasSlug(String slug) {
        return (root, query, criteriaBuilder) -> {
            if (slug == null || slug.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("slug"), slug);
        };
    }

    public static Specification<PostEntity> hasAuthorId(Long authorId) {
        return (root, query, criteriaBuilder) -> {
            if (authorId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("authorId"), authorId);
        };
    }

    public static Specification<PostEntity> createdAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<PostEntity> createdBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<PostEntity> updatedAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), date);
        };
    }

    public static Specification<PostEntity> updatedBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), date);
        };
    }

    public static Specification<PostEntity> fromFilter(br.com.neves.blog.presentation.dto.PostFilterDTO filter) {
        if (filter == null) {
            return null;
        }

        Specification<PostEntity> spec = Specification.anyOf();

        spec = spec.and(hasTitle(filter.getTitle()));
        spec = spec.and(hasContent(filter.getContent()));
        spec = spec.and(hasTag(filter.getTag()));
        spec = spec.and(hasSlug(filter.getSlug()));
        spec = spec.and(hasAuthorId(filter.getAuthorId()));
        spec = spec.and(createdAfter(filter.getCreatedAfter()));
        spec = spec.and(createdBefore(filter.getCreatedBefore()));
        spec = spec.and(updatedAfter(filter.getUpdatedAfter()));
        spec = spec.and(updatedBefore(filter.getUpdatedBefore()));

        return spec;
    }
}
