package br.com.neves.blog.application.service;

import br.com.neves.blog.domain.entity.User;
import br.com.neves.blog.domain.repository.UserRepository;
import br.com.neves.blog.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User mockUser;
    private static final String USERNAME = "testuser";
    private static final Long USER_ID = 1L;
    private static final String EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .email(EMAIL)
                .password("password123")
                .role("USER")
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should return authenticated username successfully")
    void shouldReturnAuthenticatedUsername() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);

        // Act
        String username = authService.getAuthenticatedUsername();

        // Assert
        assertNotNull(username);
        assertEquals(USERNAME, username);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
    }

    @Test
    @DisplayName("Should return authenticated user successfully")
    void shouldReturnAuthenticatedUser() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(mockUser));

        // Act
        User user = authService.getAuthenticatedUser();

        // Assert
        assertNotNull(user);
        assertEquals(USER_ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found")
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> authService.getAuthenticatedUser()
        );

        assertTrue(exception.getMessage().contains(USERNAME));
        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    @DisplayName("Should return authenticated user ID successfully")
    void shouldReturnAuthenticatedUserId() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(mockUser));

        // Act
        Long userId = authService.getAuthenticatedUserId();

        // Assert
        assertNotNull(userId);
        assertEquals(USER_ID, userId);
        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when getting user ID and user not found")
    void shouldThrowUserNotFoundExceptionWhenGettingUserId() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> authService.getAuthenticatedUserId()
        );

        assertTrue(exception.getMessage().contains(USERNAME));
        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    @DisplayName("Should handle different usernames correctly")
    void shouldHandleDifferentUsernamesCorrectly() {
        // Arrange
        String differentUsername = "anotheruser";
        User anotherUser = User.builder()
                .id(2L)
                .username(differentUsername)
                .email("another@example.com")
                .password("pass456")
                .role("ADMIN")
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(differentUsername);
        when(userRepository.findByUsername(differentUsername)).thenReturn(Optional.of(anotherUser));

        // Act
        User user = authService.getAuthenticatedUser();

        // Assert
        assertNotNull(user);
        assertEquals(differentUsername, user.getUsername());
        assertEquals(2L, user.getId());
        verify(userRepository, times(1)).findByUsername(differentUsername);
    }
}
