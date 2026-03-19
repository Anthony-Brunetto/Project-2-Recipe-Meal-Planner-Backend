package com.mealmap.exception;

public class RecipeIngredientNotFoundException extends RuntimeException {

    public RecipeIngredientNotFoundException(Long id) {
        super("Could not find recipe ingredient " + id);
    }
}
