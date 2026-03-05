package com.example.demo.hello.controller;

import com.example.demo.hello.entity.Ingredient;
import com.example.demo.hello.exception.IngredientNotFoundException;
import com.example.demo.hello.repository.IngredientRepository;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("/api/ingredients")
    List<Ingredient> all() {
        return ingredientRepository.findAll();
    }

    @PostMapping("/api/ingredients")
    Ingredient newIngredient(@RequestBody Ingredient newIngredient) {
        return ingredientRepository.save(newIngredient);
    }

    @GetMapping("/api/ingredients/{ingredientId}")
    Ingredient one(@PathVariable Long ingredientId) {
        return ingredientRepository
            .findById(ingredientId)
            .orElseThrow(() -> new IngredientNotFoundException(ingredientId));
    }

    @PutMapping("/api/ingredients/{ingredientId}")
    Ingredient replaceIngredient(
        @RequestBody Ingredient newIngredient,
        @PathVariable Long ingredientId
    ) {
        return ingredientRepository
            .findById(ingredientId)
            .map(ingredient -> {
                ingredient.setIngredientName(newIngredient.getIngredientName());
                ingredient.setDescription(newIngredient.getDescription());
                ingredient.setUnit(newIngredient.getUnit());
                ingredient.setCalories(newIngredient.getCalories());
                return ingredientRepository.save(ingredient);
            })
            .orElseGet(() -> ingredientRepository.save(newIngredient));
    }

    @DeleteMapping("/api/ingredients/{ingredientId}")
    void deleteIngredient(@PathVariable Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
    }
}
