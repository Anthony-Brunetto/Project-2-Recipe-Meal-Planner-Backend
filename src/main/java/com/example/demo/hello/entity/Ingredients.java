package com.example.demo.hello.entity;

import jakarta.persistence.*;

@Entity
public class Ingredients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredient_id;

    public Ingredients() {}

    // getters and setters
}
