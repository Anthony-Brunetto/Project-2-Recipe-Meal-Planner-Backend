package com.mealmap.controller;

import com.mealmap.entity.User;
import com.mealmap.exception.UserNotFoundException;
import com.mealmap.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/api/users/sync")
    public ResponseEntity<User> syncUser(
        @RequestBody Map<String, Object> payload
    ) {
        Map<String, Object> record = (Map<String, Object>) payload.get(
            "record"
        );

        String supabaseId = (String) record.get("id");
        String email = (String) record.get("email");
        String username = email != null ? email.split("@")[0] : "user";

        if (userRepository.existsBySupabaseId(supabaseId)) {
            return ResponseEntity.ok(
                userRepository.findBySupabaseId(supabaseId).get()
            );
        }

        Instant createdAt;
        try {
            String createdAtStr = (String) record.get("created_at");
            createdAt = Instant.parse(createdAtStr);
        } catch (Exception e) {
            createdAt = Instant.now();
        }

        User user = new User(supabaseId, email, username);
        user.setCreatedAt(createdAt);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/api/users")
    List<User> all() {
        return userRepository.findAll();
    }

    @GetMapping("/api/users/{id}")
    User one(@PathVariable Long id) {
        return userRepository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/api/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return userRepository
            .findById(id)
            .map(user -> {
                user.setUsername(newUser.getUsername());
                user.setEmail(newUser.getEmail());
                return userRepository.save(user);
            })
            .orElseGet(() -> userRepository.save(newUser));
    }

    @DeleteMapping("/api/users/{id}")
    void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
