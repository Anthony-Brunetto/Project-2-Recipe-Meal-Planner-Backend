package com.example.demo.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.example.demo.hello.entity.Users;
import com.example.demo.hello.repository.UsersRepository;


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

  @Bean
  CommandLineRunner Database(UsersRepository repo) {
      return args -> {
          repo.save(new Users("Supabase Test"));
          System.out.println("Inserted test user!");
      };
  }
}
