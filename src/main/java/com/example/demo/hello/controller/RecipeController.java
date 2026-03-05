package com.example.demo.hello.controller;

import com.example.demo.hello.entity.Recipe;
import com.example.demo.hello.exception.RecipeNotFoundException;
import com.example.demo.hello.repository.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // Get all recipes
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // Get recipe IDs
    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    // Get recipes that belong to userId #
    @GetMapping("/user/{userId}")
    public List<Recipe> getRecipesByUser(@PathVariable Long userId) {
        return recipeRepository.findByUser_UserId(userId);
}

    // Post recipes
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe newRecipe) {
        Recipe saved = recipeRepository.save(newRecipe);

        // IMPORTANT: PK getter is getRecipeId()
        return ResponseEntity
                .created(URI.create("/api/recipes/" + saved.getRecipeId()))
                .body(saved);
    }

    // Put recipe ID
    @PutMapping("/{id}")
    public Recipe updateRecipe(@RequestBody Recipe updatedRecipe, @PathVariable Long id) {
        return recipeRepository.findById(id)
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException(id);
        }
        recipeRepository.deleteById(id);
    }
}