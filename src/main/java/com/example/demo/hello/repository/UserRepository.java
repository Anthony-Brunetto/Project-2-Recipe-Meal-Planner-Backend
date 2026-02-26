package com.example.demo.hello.repository;

import com.example.demo.hello.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
