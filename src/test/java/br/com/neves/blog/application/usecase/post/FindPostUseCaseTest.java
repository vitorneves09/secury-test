package br.com.neves.blog.application.usecase.post;

import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import br.com.neves.blog.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindPostUseCase Unit Tests")
class FindPostUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private FindPostUseCase findPostUseCase;

    private Post post;
    private static final Long POST_ID = 1L;
    private static final Long AUTHOR_ID = 10L;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(POST_ID)
                .title("Test Post")
                .content("Test content for the post")
                .tag("test")
                .authorId(AUTHOR_ID)
                .created(new Date())
                .slug("test-post")
                .build();
    }

    @Test
    @DisplayName("Should find post by ID successfully")
    void shouldFindPostByIdSuccessfully() {
        // Arrange
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

        // Act
        Post result = findPostUseCase.execute(POST_ID);

        // Assert
        assertNotNull(result);
        assertEquals(POST_ID, result.getId());
        assertEquals("Test Post", result.getTitle());
        assertEquals("Test content for the post", result.getContent());
        assertEquals("test", result.getTag());
        assertEquals(AUTHOR_ID, result.getAuthorId());
        assertEquals("test-post", result.getSlug());

        verify(postRepository, times(1)).findById(POST_ID);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when post not found")
    void shouldThrowResourceNotFoundExceptionWhenPostNotFound() {
        // Arrange
        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> findPostUseCase.execute(POST_ID)
        );

        assertTrue(exception.getMessage().contains(POST_ID.toString()));
        verify(postRepository, times(1)).findById(POST_ID);
    }

    @Test
    @DisplayName("Should find post with different ID")
    void shouldFindPostWithDifferentId() {
        // Arrange
        Long differentId = 999L;
        Post differentPost = Post.builder()
                .id(differentId)
                .title("Different Post")
                .content("Different content")
                .tag("different")
                .authorId(AUTHOR_ID)
                .created(new Date())
                .build();

        when(postRepository.findById(differentId)).thenReturn(Optional.of(differentPost));

        // Act
        Post result = findPostUseCase.execute(differentId);

        // Assert
        assertNotNull(result);
        assertEquals(differentId, result.getId());
        assertEquals("Different Post", result.getTitle());
        verify(postRepository, times(1)).findById(differentId);
    }

    @Test
    @DisplayName("Should return post with all fields populated")
    void shouldReturnPostWithAllFieldsPopulated() {
        // Arrange
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

        // Act
        Post result = findPostUseCase.execute(POST_ID);

        // Assert
        assertNotNull(result.getId());
        assertNotNull(result.getTitle());
        assertNotNull(result.getContent());
        assertNotNull(result.getTag());
        assertNotNull(result.getAuthorId());
        assertNotNull(result.getCreated());
        assertNotNull(result.getSlug());
    }

    @Test
    @DisplayName("Should handle null tag in post")
    void shouldHandleNullTagInPost() {
        // Arrange
        post.setTag(null);
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));

        // Act
        Post result = findPostUseCase.execute(POST_ID);

        // Assert
        assertNotNull(result);
        assertNull(result.getTag());
        verify(postRepository, times(1)).findById(POST_ID);
    }

    @Test
    @DisplayName("Should throw exception with correct message")
    void shouldThrowExceptionWithCorrectMessage() {
        // Arrange
        Long nonExistentId = 12345L;
        when(postRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> findPostUseCase.execute(nonExistentId)
        );

        String expectedMessage = "Post not found with id: " + nonExistentId;
        assertEquals(expectedMessage, exception.getMessage());
    }
}
