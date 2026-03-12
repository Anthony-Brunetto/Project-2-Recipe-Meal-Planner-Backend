package com.example.demo.hello.exception;

public class MealPlanEntryNotFoundException extends RuntimeException {

    public MealPlanEntryNotFoundException(Long id) {
        super("Could not find a meal plan entry with id " + id);
    }
}
