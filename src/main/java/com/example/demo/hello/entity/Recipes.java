package com.example.demo.hello.entity;

import jakarta.persistence.*;

@Entity
public class Recipes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipe_id;

    public Recipes() {}

    // getters and setters
}
