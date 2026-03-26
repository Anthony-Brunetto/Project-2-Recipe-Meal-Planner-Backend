package com.mealmap.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.mealmap.entity.Recipe;
import com.mealmap.exception.RecipeNotFoundException;
import com.mealmap.repository.RecipeRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeController recipeController;

    private Recipe recipe(Long id, String name) {
        Recipe recipe = new Recipe();
        setField(recipe, "recipeId", id);
        recipe.setName(name);
        return recipe;
    }

    @Test
    void getAllRecipesReturnsAllRecipes() {
        when(recipeRepository.findAll()).thenReturn(List.of(recipe(1L, "Pasta")));

        List<Recipe> result = recipeController.getAllRecipes();

        assertEquals(1, result.size());
        assertEquals("Pasta", result.getFirst().getName());
    }

    @Test
    void newRecipeSavesAndReturnsRecipe() {
        Recipe recipe = recipe(null, "Soup");
        when(recipeRepository.save(recipe)).thenReturn(recipe);

        Recipe result = recipeController.newRecipe(recipe);

        assertEquals("Soup", result.getName());
        verify(recipeRepository).save(recipe);
    }

    @Test
    void getRecipeByIdReturnsRecipeWhenFound() {
        Recipe recipe = recipe(2L, "Salad");
        when(recipeRepository.findById(2L)).thenReturn(Optional.of(recipe));

        Recipe result = recipeController.getRecipeById(2L);

        assertEquals(2L, result.getRecipeId());
        assertEquals("Salad", result.getName());
    }

    @Test
    void getRecipeByIdThrowsWhenNotFound() {
        when(recipeRepository.findById(12L)).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () ->
            recipeController.getRecipeById(12L)
        );
    }

    @Test
    void updateRecipeUpdatesExistingRecipe() {
        Recipe existing = recipe(5L, "Old");
        Recipe update = recipe(null, "New");

        when(recipeRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation ->
            invocation.getArgument(0)
        );

        Recipe result = recipeController.updateRecipe(update, 5L);

        assertEquals("New", result.getName());
        verify(recipeRepository).save(existing);
    }

    @Test
    void deleteRecipeDeletesWhenRecipeExists() {
        when(recipeRepository.existsById(6L)).thenReturn(true);

        recipeController.deleteRecipe(6L);

        verify(recipeRepository).deleteById(6L);
    }

    @Test
    void deleteRecipeThrowsWhenRecipeMissing() {
        when(recipeRepository.existsById(66L)).thenReturn(false);

        assertThrows(RecipeNotFoundException.class, () ->
            recipeController.deleteRecipe(66L)
        );
    }
}
