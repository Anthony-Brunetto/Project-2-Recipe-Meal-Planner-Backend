package com.mealmap.controller;

import com.mealmap.entity.Recipe;
import com.mealmap.exception.RecipeNotFoundException;
import com.mealmap.repository.RecipeRepository;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // Get all recipes
    @GetMapping("/api/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @PostMapping("/api/recipes")
    Recipe newRecipe(@RequestBody Recipe newRecipe) {
        return recipeRepository.save(newRecipe);
    }

    // Get recipe IDs
    @GetMapping("/api/recipes/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeRepository
            .findById(id)
            .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    // Get recipes that belong to userId #
    @GetMapping("/api/recipes/users/{userId}")
    public List<Recipe> getRecipesByUser(@PathVariable Long userId) {
        return recipeRepository.findByUser_UserId(userId);
    }

    // Put recipe ID
    @PutMapping("/api/recipes/{id}")
    public Recipe updateRecipe(
        @RequestBody Recipe updatedRecipe,
        @PathVariable Long id
    ) {
        return recipeRepository
            .findById(id)
            .map(existing -> {
                existing.setName(updatedRecipe.getName());
                existing.setDescription(updatedRecipe.getDescription());
                existing.setInstructions(updatedRecipe.getInstructions());
                existing.setOriginalUser(updatedRecipe.getOriginalUser());

                return recipeRepository.save(existing);
            })
            .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    // Delete recipe ID
    @DeleteMapping("/api/recipes/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException(id);
        }
        recipeRepository.deleteById(id);
    }
}
