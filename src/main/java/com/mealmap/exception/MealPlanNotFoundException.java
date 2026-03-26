package com.mealmap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MealPlanNotFoundException extends RuntimeException {

    public MealPlanNotFoundException(Long id) {
        super("Could not find a meal plan with id " + id);
    }
}
