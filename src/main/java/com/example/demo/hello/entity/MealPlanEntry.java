package com.example.demo.hello.entity;

import jakarta.persistence.*;

@Entity
public class MealPlanEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entry_id;

    public MealPlanEntry() {}

    // getters and setters
}
