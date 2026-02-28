package com.example.demo.hello;

import com.example.demo.hello.entity.User;
import com.example.demo.hello.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    // @Bean
    // CommandLineRunner database(UserRepository repo) {
    //     return args -> {
    //         User user = new User();
    //         user.setUsername("testuser");
    //         user.setPasswordHash("testpasswordhash");
    //         repo.save(user);
    //         System.out.println("Inserted test user!");
    //     };
    // }
}
