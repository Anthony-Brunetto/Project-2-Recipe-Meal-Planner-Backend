package com.example.demo.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.hello.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
