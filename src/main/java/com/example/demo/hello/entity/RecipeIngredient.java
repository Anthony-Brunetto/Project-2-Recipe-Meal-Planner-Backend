package com.example.demo.hello.entity;

import jakarta.persistence.*;

@Entity
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipe_id;

    public RecipeIngredient() {}

    // getters and setters
}
