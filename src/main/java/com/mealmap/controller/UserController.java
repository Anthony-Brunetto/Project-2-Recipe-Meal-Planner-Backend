package com.mealmap.controller;

import com.mealmap.entity.User;
import com.mealmap.exception.UserNotFoundException;
import com.mealmap.repository.UserRepository;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/users")
    List<User> all() {
        return userRepository.findAll();
    }

    @PostMapping("/api/users")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
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
                return userRepository.save(user);
            })
            .orElseGet(() -> {
                return userRepository.save(newUser);
            });
    }

    @DeleteMapping("/api/users/{id}")
    void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
