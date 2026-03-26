package com.mealmap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecipeIngredientNotFoundException extends RuntimeException {

    public RecipeIngredientNotFoundException(Long id) {
        super("Could not find recipe ingredient " + id);
    }
}
