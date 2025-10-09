package br.com.neves.blog.application.usecase.post;

import br.com.neves.blog.application.service.AuthService;
import br.com.neves.blog.domain.entity.Post;
import br.com.neves.blog.domain.entity.User;
import br.com.neves.blog.domain.repository.PostRepository;
import br.com.neves.blog.exception.NotPermissionException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdatePostUseCase Unit Tests")
class UpdatePostUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UpdatePostUseCase updatePostUseCase;

    private Post existingPost;
    private Post updatedPostData;
    private User author;
    private static final Long POST_ID = 1L;
    private static final Long AUTHOR_ID = 10L;
    private static final Long OTHER_USER_ID = 20L;

    @BeforeEach
    void setUp() {
        existingPost = Post.builder()
                .id(POST_ID)
                .title("Original Title")
                .content("Original content")
                .tag("original-tag")
                .authorId(AUTHOR_ID)
                .created(new Date())
                .build();

        updatedPostData = Post.builder()
                .title("Updated Title")
                .content("Updated content")
                .tag("updated-tag")
                .build();

        author = User.builder()
                .id(AUTHOR_ID)
                .username("author")
                .email("author@example.com")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("Should update post successfully when user is author")
    void shouldUpdatePostSuccessfullyWhenUserIsAuthor() {
        // Arrange
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(existingPost));
        when(authService.getAuthenticatedUser()).thenReturn(author);
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        // Act
        Post result = updatePostUseCase.execute(POST_ID, updatedPostData);

        // Assert
        assertNotNull(result);
        verify(postRepository, times(1)).findById(POST_ID);
        verify(authService, times(1)).getAuthenticatedUser();
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("Should update post fields correctly")
    void shouldUpdatePostFieldsCorrectly() {
        // Arrange
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(existingPost));
        when(authService.getAuthenticatedUser()).thenReturn(author);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Post result = updatePostUseCase.execute(POST_ID, updatedPostData);

        // Assert
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated content", result.getContent());
        assertEquals("updated-tag", result.getTag());
        assertEquals(AUTHOR_ID, result.getAuthorId());
        assertEquals(POST_ID, result.getId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when post not found")
    void shouldThrowResourceNotFoundExceptionWhenPostNotFound() {
        // Arrange
        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            updatePostUseCase.execute(POST_ID, updatedPostData);
        });

        verify(postRepository, times(1)).findById(POST_ID);
        verify(authService, never()).getAuthenticatedUser();
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("Should throw NotPermissionException when user is not author")
    void shouldThrowNotPermissionExceptionWhenUserIsNotAuthor() {
        // Arrange
        User otherUser = User.builder()
                .id(OTHER_USER_ID)
                .username("otheruser")
                .email("other@example.com")
                .role("USER")
                .build();

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(existingPost));
        when(authService.getAuthenticatedUser()).thenReturn(otherUser);

        // Act & Assert
        assertThrows(NotPermissionException.class, () -> {
            updatePostUseCase.execute(POST_ID, updatedPostData);
        });

        verify(postRepository, times(1)).findById(POST_ID);
        verify(authService, times(1)).getAuthenticatedUser();
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("Should allow admin to update any post")
    void shouldAllowAdminToUpdateAnyPost() {
        // Arrange
        User admin = User.builder()
                .id(OTHER_USER_ID)
                .username("admin")
                .email("admin@example.com")
                .role("ADMIN")
                .build();

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(existingPost));
        when(authService.getAuthenticatedUser()).thenReturn(admin);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Post result = updatePostUseCase.execute(POST_ID, updatedPostData);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("Should preserve authorId when updating")
    void shouldPreserveAuthorIdWhenUpdating() {
        // Arrange
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(existingPost));
        when(authService.getAuthenticatedUser()).thenReturn(author);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Post result = updatePostUseCase.execute(POST_ID, updatedPostData);

        // Assert
        assertEquals(AUTHOR_ID, result.getAuthorId());
        assertNotEquals(OTHER_USER_ID, result.getAuthorId());
    }

    @Test
    @DisplayName("Should preserve post ID when updating")
    void shouldPreservePostIdWhenUpdating() {
        // Arrange
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(existingPost));
        when(authService.getAuthenticatedUser()).thenReturn(author);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Post result = updatePostUseCase.execute(POST_ID, updatedPostData);

        // Assert
        assertEquals(POST_ID, result.getId());
    }

    @Test
    @DisplayName("Should handle null tag correctly")
    void shouldHandleNullTagCorrectly() {
        // Arrange
        updatedPostData.setTag(null);
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(existingPost));
        when(authService.getAuthenticatedUser()).thenReturn(author);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Post result = updatePostUseCase.execute(POST_ID, updatedPostData);

        // Assert
        assertNull(result.getTag());
        verify(postRepository, times(1)).save(any(Post.class));
    }
}
