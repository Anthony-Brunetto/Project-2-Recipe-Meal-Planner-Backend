package com.mealmap.controller;

import com.mealmap.entity.RecipeIngredient;
import com.mealmap.exception.IngredientNotFoundException;
import com.mealmap.exception.RecipeIngredientNotFoundException;
import com.mealmap.exception.RecipeNotFoundException;
import com.mealmap.repository.IngredientRepository;
import com.mealmap.repository.RecipeIngredientRepository;
import com.mealmap.repository.RecipeRepository;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeIngredientController {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeIngredientController(
        RecipeIngredientRepository recipeIngredientRepository,
        RecipeRepository recipeRepository,
        IngredientRepository ingredientRepository
    ) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("/api/recipe-ingredients")
    public List<RecipeIngredient> getAllRecipeIngredients() {
        return recipeIngredientRepository.findAll();
    }

    @GetMapping("/api/recipe-ingredients/{id}")
    public RecipeIngredient getRecipeIngredientById(@PathVariable Long id) {
        return recipeIngredientRepository
            .findById(id)
            .orElseThrow(() -> new RecipeIngredientNotFoundException(id));
    }

    @GetMapping("/api/recipe-ingredients/recipe/{recipeId}")
    public List<RecipeIngredient> getRecipeIngredientsByRecipe(
        @PathVariable Long recipeId
    ) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new RecipeNotFoundException(recipeId);
        }

        return recipeIngredientRepository.findByRecipe_RecipeId(recipeId);
    }

    @PostMapping("/api/recipe-ingredients")
    public ResponseEntity<RecipeIngredient> createRecipeIngredient(
        @RequestBody RecipeIngredient newRecipeIngredient
    ) {
        attachReferences(newRecipeIngredient);
        RecipeIngredient saved = recipeIngredientRepository.save(
            newRecipeIngredient
        );

        return ResponseEntity.created(
            URI.create(
                "/api/recipe-ingredients/" + saved.getRecipeIngredientId()
            )
        ).body(saved);
    }

    @PutMapping("/api/recipe-ingredients/{id}")
    public RecipeIngredient updateRecipeIngredient(
        @RequestBody RecipeIngredient updatedRecipeIngredient,
        @PathVariable Long id
    ) {
        return recipeIngredientRepository
            .findById(id)
            .map(existing -> {
                existing.setRecipe(getRecipe(updatedRecipeIngredient));
                existing.setIngredient(getIngredient(updatedRecipeIngredient));
                existing.setQuantity(updatedRecipeIngredient.getQuantity());
                existing.setUnit(updatedRecipeIngredient.getUnit());

                return recipeIngredientRepository.save(existing);
            })
            .orElseThrow(() -> new RecipeIngredientNotFoundException(id));
    }

    @DeleteMapping("/api/recipe-ingredients/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeIngredient(@PathVariable Long id) {
        if (!recipeIngredientRepository.existsById(id)) {
            throw new RecipeIngredientNotFoundException(id);
        }

        recipeIngredientRepository.deleteById(id);
    }

    private void attachReferences(RecipeIngredient recipeIngredient) {
        recipeIngredient.setRecipe(getRecipe(recipeIngredient));
        recipeIngredient.setIngredient(getIngredient(recipeIngredient));
    }

    private com.mealmap.entity.Recipe getRecipe(
        RecipeIngredient recipeIngredient
    ) {
        if (
            recipeIngredient.getRecipe() == null ||
            recipeIngredient.getRecipe().getRecipeId() == null
        ) {
            throw new RecipeNotFoundException(null);
        }

        Long recipeId = recipeIngredient.getRecipe().getRecipeId();
        return recipeRepository
            .findById(recipeId)
            .orElseThrow(() -> new RecipeNotFoundException(recipeId));
    }

    private com.mealmap.entity.Ingredient getIngredient(
        RecipeIngredient recipeIngredient
    ) {
        if (
            recipeIngredient.getIngredient() == null ||
            recipeIngredient.getIngredient().getIngredientId() == null
        ) {
            throw new IngredientNotFoundException(null);
        }

        Long ingredientId = recipeIngredient.getIngredient().getIngredientId();
        return ingredientRepository
            .findById(ingredientId)
            .orElseThrow(() -> new IngredientNotFoundException(ingredientId));
    }
}
