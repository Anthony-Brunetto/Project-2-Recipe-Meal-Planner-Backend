package com.example.demo.hello;

import com.example.demo.hello.entity.User;
import com.example.demo.hello.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    @RequestMapping("/")
    public String home() {
        return "FOOD API GOES HERE!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

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
