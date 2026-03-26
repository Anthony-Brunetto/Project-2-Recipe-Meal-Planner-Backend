package com.mealmap.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.mealmap.entity.Ingredient;
import com.mealmap.exception.IngredientNotFoundException;
import com.mealmap.repository.IngredientRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientController ingredientController;

    private Ingredient ingredient(Long id, String name) {
        Ingredient ingredient = new Ingredient();
        setField(ingredient, "ingredientId", id);
        ingredient.setIngredientName(name);
        return ingredient;
    }

    @Test
    void allReturnsAllIngredients() {
        Ingredient ingredient = ingredient(1L, "Egg");
        when(ingredientRepository.findAll()).thenReturn(List.of(ingredient));

        List<Ingredient> result = ingredientController.all();

        assertEquals(1, result.size());
        assertEquals("Egg", result.getFirst().getIngredientName());
    }

    @Test
    void oneThrowsWhenNotFound() {
        when(ingredientRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(IngredientNotFoundException.class, () ->
            ingredientController.one(9L)
        );
    }

    @Test
    void newIngredientSavesAndReturnsEntity() {
        Ingredient ingredient = ingredient(null, "Rice");
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);

        Ingredient result = ingredientController.newIngredient(ingredient);

        assertEquals("Rice", result.getIngredientName());
        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void replaceIngredientUpdatesExistingIngredient() {
        Ingredient existing = ingredient(2L, "Old");
        Ingredient update = ingredient(null, "New");

        when(ingredientRepository.findById(2L)).thenReturn(
            Optional.of(existing)
        );
        when(ingredientRepository.save(any(Ingredient.class))).thenAnswer(
            invocation -> invocation.getArgument(0)
        );

        Ingredient result = ingredientController.replaceIngredient(update, 2L);

        assertEquals("New", result.getIngredientName());
        verify(ingredientRepository).save(existing);
    }

    @Test
    void deleteIngredientDeletesById() {
        ingredientController.deleteIngredient(3L);
        verify(ingredientRepository).deleteById(3L);
    }

    @Test
    void replaceIngredientCreatesNewWhenNotFound() {
        Ingredient update = ingredient(null, "New");

        when(ingredientRepository.findById(99L)).thenReturn(Optional.empty());
        when(ingredientRepository.save(any(Ingredient.class))).thenAnswer(
            invocation -> invocation.getArgument(0)
        );

        Ingredient result = ingredientController.replaceIngredient(update, 99L);

        assertEquals("New", result.getIngredientName());
        verify(ingredientRepository).save(update);
    }

    @Test
    void allReturnsEmptyListWhenNoIngredients() {
        when(ingredientRepository.findAll()).thenReturn(List.of());

        List<Ingredient> result = ingredientController.all();

        assertTrue(result.isEmpty());
    }

    @Test
    void oneReturnsIngredientWhenFound() {
        Ingredient ingredient = ingredient(1L, "Egg");
        when(ingredientRepository.findById(1L)).thenReturn(
            Optional.of(ingredient)
        );

        Ingredient result = ingredientController.one(1L);

        assertEquals("Egg", result.getIngredientName());
    }
}
