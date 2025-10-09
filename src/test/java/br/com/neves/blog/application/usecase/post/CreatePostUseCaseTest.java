package br.com.neves.blog.application.usecase.post;

import br.com.neves.blog.application.service.AuthService;
import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.repository.PostRepository;
import br.com.neves.blog.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreatePostUseCase Unit Tests")
class CreatePostUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private CreatePostUseCase createPostUseCase;

    private Post inputPost;
    private Post savedPost;
    private static final Long USER_ID = 1L;
    private static final String USERNAME = "testuser";
    private static final Long POST_ID = 100L;

    @BeforeEach
    void setUp() {
        inputPost = Post.builder()
                .title("Test Post Title")
                .content("Test post content with more than 20 characters")
                .tag("technology")
                .build();

        savedPost = Post.builder()
                .id(POST_ID)
                .title("Test Post Title")
                .content("Test post content with more than 20 characters")
                .tag("technology")
                .authorId(USER_ID)
                .created(new Date())
                .build();
    }

    @Test
    @DisplayName("Should create post successfully")
    void shouldCreatePostSuccessfully() {
        // Arrange
        when(authService.getAuthenticatedUsername()).thenReturn(USERNAME);
        when(authService.getAuthenticatedUserId()).thenReturn(USER_ID);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // Act
        Post result = createPostUseCase.execute(inputPost);

        // Assert
        assertNotNull(result);
        assertEquals(POST_ID, result.getId());
        assertEquals(USER_ID, result.getAuthorId());
        assertNotNull(result.getCreated());
        assertEquals("Test Post Title", result.getTitle());
        assertEquals("Test post content with more than 20 characters", result.getContent());
        assertEquals("technology", result.getTag());

        verify(authService, times(1)).getAuthenticatedUsername();
        verify(authService, times(1)).getAuthenticatedUserId();
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("Should set authorId from authenticated user")
    void shouldSetAuthorIdFromAuthenticatedUser() {
        // Arrange
        when(authService.getAuthenticatedUsername()).thenReturn(USERNAME);
        when(authService.getAuthenticatedUserId()).thenReturn(USER_ID);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        // Act
        createPostUseCase.execute(inputPost);

        // Assert
        verify(postRepository).save(postCaptor.capture());
        Post capturedPost = postCaptor.getValue();
        assertEquals(USER_ID, capturedPost.getAuthorId());
    }

    @Test
    @DisplayName("Should set created date when creating post")
    void shouldSetCreatedDateWhenCreatingPost() {
        // Arrange
        when(authService.getAuthenticatedUsername()).thenReturn(USERNAME);
        when(authService.getAuthenticatedUserId()).thenReturn(USER_ID);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        // Act
        createPostUseCase.execute(inputPost);

        // Assert
        verify(postRepository).save(postCaptor.capture());
        Post capturedPost = postCaptor.getValue();
        assertNotNull(capturedPost.getCreated());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found")
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        // Arrange
        when(authService.getAuthenticatedUsername()).thenReturn(USERNAME);
        when(authService.getAuthenticatedUserId()).thenThrow(new UserNotFoundException(USERNAME));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            createPostUseCase.execute(inputPost);
        });

        verify(authService, times(1)).getAuthenticatedUsername();
        verify(authService, times(1)).getAuthenticatedUserId();
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("Should preserve post title and content")
    void shouldPreservePostTitleAndContent() {
        // Arrange
        when(authService.getAuthenticatedUsername()).thenReturn(USERNAME);
        when(authService.getAuthenticatedUserId()).thenReturn(USER_ID);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        // Act
        createPostUseCase.execute(inputPost);

        // Assert
        verify(postRepository).save(postCaptor.capture());
        Post capturedPost = postCaptor.getValue();
        assertEquals(inputPost.getTitle(), capturedPost.getTitle());
        assertEquals(inputPost.getContent(), capturedPost.getContent());
        assertEquals(inputPost.getTag(), capturedPost.getTag());
    }

    @Test
    @DisplayName("Should handle different user IDs correctly")
    void shouldHandleDifferentUserIdsCorrectly() {
        // Arrange
        Long differentUserId = 999L;
        Post differentSavedPost = Post.builder()
                .id(POST_ID)
                .title("Test Post Title")
                .content("Test post content with more than 20 characters")
                .tag("technology")
                .authorId(differentUserId)
                .created(new Date())
                .build();

        when(authService.getAuthenticatedUsername()).thenReturn("anotheruser");
        when(authService.getAuthenticatedUserId()).thenReturn(differentUserId);
        when(postRepository.save(any(Post.class))).thenReturn(differentSavedPost);

        // Act
        Post result = createPostUseCase.execute(inputPost);

        // Assert
        assertNotNull(result);
        assertEquals(differentUserId, result.getAuthorId());
        verify(authService, times(1)).getAuthenticatedUserId();
    }
}
