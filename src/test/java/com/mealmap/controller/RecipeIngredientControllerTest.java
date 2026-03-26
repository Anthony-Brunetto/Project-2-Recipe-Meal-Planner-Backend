package com.mealmap.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.mealmap.entity.Ingredient;
import com.mealmap.entity.Recipe;
import com.mealmap.entity.RecipeIngredient;
import com.mealmap.exception.RecipeIngredientNotFoundException;
import com.mealmap.exception.RecipeNotFoundException;
import com.mealmap.repository.IngredientRepository;
import com.mealmap.repository.RecipeIngredientRepository;
import com.mealmap.repository.RecipeRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RecipeIngredientControllerTest {

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeIngredientController recipeIngredientController;

    private Recipe recipe(Long id) {
        Recipe recipe = new Recipe();
        setField(recipe, "recipeId", id);
        return recipe;
    }

    private Ingredient ingredient(Long id) {
        Ingredient ingredient = new Ingredient();
        setField(ingredient, "ingredientId", id);
        return ingredient;
    }

    private RecipeIngredient recipeIngredient(Long id) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        setField(recipeIngredient, "recipeIngredientId", id);
        return recipeIngredient;
    }

    @Test
    void getAllRecipeIngredientsReturnsAllRecords() {
        RecipeIngredient record = recipeIngredient(1L);
        when(recipeIngredientRepository.findAll()).thenReturn(List.of(record));

        List<RecipeIngredient> result =
            recipeIngredientController.getAllRecipeIngredients();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getRecipeIngredientId());
    }

    @Test
    void getRecipeIngredientByIdReturnsRecordWhenFound() {
        when(recipeIngredientRepository.findById(2L)).thenReturn(
            Optional.of(recipeIngredient(2L))
        );

        RecipeIngredient result = recipeIngredientController.getRecipeIngredientById(2L);

        assertEquals(2L, result.getRecipeIngredientId());
    }

    @Test
    void getRecipeIngredientByIdThrowsWhenMissing() {
        when(recipeIngredientRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(RecipeIngredientNotFoundException.class, () ->
            recipeIngredientController.getRecipeIngredientById(20L)
        );
    }

    @Test
    void getRecipeIngredientsByRecipeReturnsRecordsWhenRecipeExists() {
        when(recipeRepository.existsById(8L)).thenReturn(true);
        when(recipeIngredientRepository.findByRecipe_RecipeId(8L)).thenReturn(
            List.of(recipeIngredient(3L))
        );

        List<RecipeIngredient> result =
            recipeIngredientController.getRecipeIngredientsByRecipe(8L);

        assertEquals(1, result.size());
        verify(recipeIngredientRepository).findByRecipe_RecipeId(8L);
    }

    @Test
    void getRecipeIngredientsByRecipeThrowsWhenRecipeMissing() {
        when(recipeRepository.existsById(9L)).thenReturn(false);

        assertThrows(RecipeNotFoundException.class, () ->
            recipeIngredientController.getRecipeIngredientsByRecipe(9L)
        );
    }

    @Test
    void createRecipeIngredientResolvesReferencesAndReturnsCreatedResponse() {
        RecipeIngredient payload = recipeIngredient(null);
        payload.setRecipe(recipe(10L));
        payload.setIngredient(ingredient(11L));

        RecipeIngredient saved = recipeIngredient(50L);

        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe(10L)));
        when(ingredientRepository.findById(11L)).thenReturn(Optional.of(ingredient(11L)));
        when(recipeIngredientRepository.save(any(RecipeIngredient.class))).thenReturn(
            saved
        );

        ResponseEntity<RecipeIngredient> response =
            recipeIngredientController.createRecipeIngredient(payload);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(
            "/api/recipe-ingredients/50",
            response.getHeaders().getLocation().getPath()
        );
        assertEquals(50L, response.getBody().getRecipeIngredientId());
    }

    @Test
    void updateRecipeIngredientThrowsWhenMissing() {
        RecipeIngredient update = recipeIngredient(null);
        when(recipeIngredientRepository.findById(31L)).thenReturn(Optional.empty());

        assertThrows(RecipeIngredientNotFoundException.class, () ->
            recipeIngredientController.updateRecipeIngredient(update, 31L)
        );
    }

    @Test
    void deleteRecipeIngredientDeletesWhenRecordExists() {
        when(recipeIngredientRepository.existsById(40L)).thenReturn(true);

        recipeIngredientController.deleteRecipeIngredient(40L);

        verify(recipeIngredientRepository).deleteById(40L);
    }

    @Test
    void deleteRecipeIngredientThrowsWhenRecordMissing() {
        when(recipeIngredientRepository.existsById(41L)).thenReturn(false);

        assertThrows(RecipeIngredientNotFoundException.class, () ->
            recipeIngredientController.deleteRecipeIngredient(41L)
        );
    }
}
