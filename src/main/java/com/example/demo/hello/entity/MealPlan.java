package com.example.demo.hello.entity;

import jakarta.persistence.*;

@Entity
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meal_plan_id;

    public MealPlan() {}

    // getters and setters
}
