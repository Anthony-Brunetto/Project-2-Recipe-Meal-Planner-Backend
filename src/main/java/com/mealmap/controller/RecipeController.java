package com.mealmap.controller;

import com.mealmap.dto.FeaturedRecipeResponse;
import com.mealmap.entity.Recipe;
import com.mealmap.exception.RecipeNotFoundException;
import com.mealmap.repository.RecipeRepository;
import com.mealmap.service.RecipeService;
import java.time.Duration;
import java.util.List;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;

    public RecipeController(
        RecipeRepository recipeRepository,
        RecipeService recipeService
    ) {
        this.recipeRepository = recipeRepository;
        this.recipeService = recipeService;
    }

    // Get all recipes
    @GetMapping("/api/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @GetMapping("/api/recipes/featured/today")
    public ResponseEntity<List<FeaturedRecipeResponse>> getFeaturedRecipesToday() {
        Duration cacheTtl = recipeService.getCacheTtl();

        // Featured recipes rotate on the current date in one explicit timezone so
        // every request for that day returns the same deterministic selection.
        return ResponseEntity
            .ok()
            .cacheControl(CacheControl.maxAge(cacheTtl).cachePublic())
            .header(HttpHeaders.VARY, "Accept-Encoding")
            .body(recipeService.getTodayFeaturedRecipes());
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
    @GetMapping("/api/recipes/{userId}")
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
