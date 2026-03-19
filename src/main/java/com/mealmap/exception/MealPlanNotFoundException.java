package com.mealmap.exception;

public class MealPlanNotFoundException extends RuntimeException {

    public MealPlanNotFoundException(Long id) {
        super("Could not find a meal plan with id " + id);
    }
}
