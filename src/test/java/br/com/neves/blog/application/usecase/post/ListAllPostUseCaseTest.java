package br.com.neves.blog.application.usecase.post;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListAllPostUseCase Unit Tests")
class ListAllPostUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private ListAllPostUseCase listAllPostUseCase;

    private List<Post> posts;

    @BeforeEach
    void setUp() {
        Post post1 = Post.builder()
                .id(1L)
                .title("First Post")
                .content("Content of first post")
                .tag("tech")
                .authorId(10L)
                .created(new Date())
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .title("Second Post")
                .content("Content of second post")
                .tag("business")
                .authorId(20L)
                .created(new Date())
                .build();

        Post post3 = Post.builder()
                .id(3L)
                .title("Third Post")
                .content("Content of third post")
                .tag("lifestyle")
                .authorId(10L)
                .created(new Date())
                .build();

        posts = Arrays.asList(post1, post2, post3);
    }

    @Test
    @DisplayName("Should return all posts successfully")
    void shouldReturnAllPostsSuccessfully() {
        // Arrange
        when(postRepository.findAll()).thenReturn(posts);

        // Act
        List<Post> result = listAllPostUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("First Post", result.get(0).getTitle());
        assertEquals("Second Post", result.get(1).getTitle());
        assertEquals("Third Post", result.get(2).getTitle());

        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no posts exist")
    void shouldReturnEmptyListWhenNoPostsExist() {
        // Arrange
        when(postRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Post> result = listAllPostUseCase.execute();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return list with correct size")
    void shouldReturnListWithCorrectSize() {
        // Arrange
        when(postRepository.findAll()).thenReturn(posts);

        // Act
        List<Post> result = listAllPostUseCase.execute();

        // Assert
        assertEquals(posts.size(), result.size());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return posts with all fields populated")
    void shouldReturnPostsWithAllFieldsPopulated() {
        // Arrange
        when(postRepository.findAll()).thenReturn(posts);

        // Act
        List<Post> result = listAllPostUseCase.execute();

        // Assert
        result.forEach(post -> {
            assertNotNull(post.getId());
            assertNotNull(post.getTitle());
            assertNotNull(post.getContent());
            assertNotNull(post.getAuthorId());
            assertNotNull(post.getCreated());
        });

        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return posts from different authors")
    void shouldReturnPostsFromDifferentAuthors() {
        // Arrange
        when(postRepository.findAll()).thenReturn(posts);

        // Act
        List<Post> result = listAllPostUseCase.execute();

        // Assert
        assertTrue(result.stream().anyMatch(p -> p.getAuthorId().equals(10L)));
        assertTrue(result.stream().anyMatch(p -> p.getAuthorId().equals(20L)));
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle single post in list")
    void shouldHandleSinglePostInList() {
        // Arrange
        Post singlePost = posts.get(0);
        when(postRepository.findAll()).thenReturn(Collections.singletonList(singlePost));

        // Act
        List<Post> result = listAllPostUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("First Post", result.get(0).getTitle());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return list with posts having different tags")
    void shouldReturnListWithPostsHavingDifferentTags() {
        // Arrange
        when(postRepository.findAll()).thenReturn(posts);

        // Act
        List<Post> result = listAllPostUseCase.execute();

        // Assert
        assertTrue(result.stream().anyMatch(p -> "tech".equals(p.getTag())));
        assertTrue(result.stream().anyMatch(p -> "business".equals(p.getTag())));
        assertTrue(result.stream().anyMatch(p -> "lifestyle".equals(p.getTag())));
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should call repository findAll exactly once")
    void shouldCallRepositoryFindAllExactlyOnce() {
        // Arrange
        when(postRepository.findAll()).thenReturn(posts);

        // Act
        listAllPostUseCase.execute();

        // Assert
        verify(postRepository, times(1)).findAll();
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("Should return list preserving order")
    void shouldReturnListPreservingOrder() {
        // Arrange
        when(postRepository.findAll()).thenReturn(posts);

        // Act
        List<Post> result = listAllPostUseCase.execute();

        // Assert
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(3L, result.get(2).getId());
        verify(postRepository, times(1)).findAll();
    }
}
