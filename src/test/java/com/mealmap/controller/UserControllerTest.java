package com.mealmap.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.mealmap.entity.User;
import com.mealmap.exception.UserNotFoundException;
import com.mealmap.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private User user(
        Long id,
        String supabaseId,
        String email,
        String username
    ) {
        User user = new User(supabaseId, email, username);
        setField(user, "userId", id);
        return user;
    }

    @Test
    void syncUserCreatesNewUserWhenNotExists() {
        Map<String, Object> payload = Map.of(
            "record",
            Map.of(
                "id",
                "supabase-123",
                "email",
                "john@example.com",
                "created_at",
                "2026-03-25T00:00:00Z"
            )
        );

        when(userRepository.existsBySupabaseId("supabase-123")).thenReturn(
            false
        );
        when(userRepository.save(any(User.class))).thenAnswer(i ->
            i.getArgument(0)
        );

        ResponseEntity<User> response = userController.syncUser(payload);

        assertEquals("john@example.com", response.getBody().getEmail());
        assertEquals("john", response.getBody().getUsername());
        assertEquals("supabase-123", response.getBody().getSupabaseId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void syncUserReturnsExistingUserWhenAlreadySynced() {
        User existing = user(1L, "supabase-123", "john@example.com", "john");

        Map<String, Object> payload = Map.of(
            "record",
            Map.of(
                "id",
                "supabase-123",
                "email",
                "john@example.com",
                "created_at",
                "2026-03-25T00:00:00Z"
            )
        );

        when(userRepository.existsBySupabaseId("supabase-123")).thenReturn(
            true
        );
        when(userRepository.findBySupabaseId("supabase-123")).thenReturn(
            Optional.of(existing)
        );

        ResponseEntity<User> response = userController.syncUser(payload);

        assertEquals("john@example.com", response.getBody().getEmail());
        verify(userRepository, never()).save(any(User.class)); // should NOT create a duplicate
    }

    @Test
    void syncUserFallsBackToNowWhenCreatedAtIsMalformed() {
        Map<String, Object> payload = Map.of(
            "record",
            Map.of(
                "id",
                "supabase-456",
                "email",
                "jane@example.com",
                "created_at",
                "not-a-valid-timestamp"
            )
        );

        when(userRepository.existsBySupabaseId("supabase-456")).thenReturn(
            false
        );
        when(userRepository.save(any(User.class))).thenAnswer(i ->
            i.getArgument(0)
        );

        Instant before = Instant.now();
        ResponseEntity<User> response = userController.syncUser(payload);
        Instant after = Instant.now();

        assertNotNull(response.getBody().getCreatedAt());
        assertTrue(!response.getBody().getCreatedAt().isBefore(before));
        assertTrue(!response.getBody().getCreatedAt().isAfter(after));
    }

    @Test
    void syncUserDerivesUsernameFromEmail() {
        Map<String, Object> payload = Map.of(
            "record",
            Map.of(
                "id",
                "supabase-789",
                "email",
                "testuser@gmail.com",
                "created_at",
                "2026-03-25T00:00:00Z"
            )
        );

        when(userRepository.existsBySupabaseId("supabase-789")).thenReturn(
            false
        );
        when(userRepository.save(any(User.class))).thenAnswer(i ->
            i.getArgument(0)
        );

        ResponseEntity<User> response = userController.syncUser(payload);

        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void syncUserSetsUsernameToUserWhenEmailIsNull() {
        Map<String, Object> payload = Map.of(
            "record",
            Map.of("id", "supabase-000", "created_at", "2026-03-25T00:00:00Z")
        );

        when(userRepository.existsBySupabaseId("supabase-000")).thenReturn(
            false
        );
        when(userRepository.save(any(User.class))).thenAnswer(i ->
            i.getArgument(0)
        );

        ResponseEntity<User> response = userController.syncUser(payload);

        assertEquals("user", response.getBody().getUsername());
    }

    @Test
    void allReturnsAllUsers() {
        User u = user(1L, "supabase-123", "john@example.com", "john");
        when(userRepository.findAll()).thenReturn(List.of(u));

        List<User> result = userController.all();

        assertEquals(1, result.size());
        assertEquals("john@example.com", result.getFirst().getEmail());
    }

    @Test
    void allReturnsEmptyListWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = userController.all();

        assertTrue(result.isEmpty());
    }

    @Test
    void oneReturnsUserById() {
        User u = user(1L, "supabase-123", "john@example.com", "john");
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        User result = userController.one(1L);

        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void oneThrowsWhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
            userController.one(99L)
        );
    }

    @Test
    void replaceUserUpdatesExistingUser() {
        User existing = user(1L, "supabase-123", "old@example.com", "oldname");
        User update = new User(null, "new@example.com", "newname");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i ->
            i.getArgument(0)
        );

        User result = userController.replaceUser(update, 1L);

        assertEquals("newname", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository).save(existing);
    }

    @Test
    void replaceUserCreatesNewUserWhenNotFound() {
        User update = new User("supabase-new", "new@example.com", "newuser");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i ->
            i.getArgument(0)
        );

        User result = userController.replaceUser(update, 99L);

        assertEquals("newuser", result.getUsername());
        verify(userRepository).save(update);
    }

    @Test
    void deleteUserDeletesById() {
        userController.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}
