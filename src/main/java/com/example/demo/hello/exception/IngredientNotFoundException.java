package com.example.demo.hello.exception;

public class IngredientNotFoundException extends RuntimeException {

    public IngredientNotFoundException(Long id) {
        super("Could not find an ingredient with id " + id);
    }
}
