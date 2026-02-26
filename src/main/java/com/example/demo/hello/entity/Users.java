package com.example.demo.hello.entity;

import jakarta.persistence.*;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String username;
    private String password_hash;

    public Users() {}

    public Users(String username) {
        this.username = username;
    }

    // getters and setters
}
