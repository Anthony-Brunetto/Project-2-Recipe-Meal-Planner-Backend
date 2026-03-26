package com.mealmap.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.mealmap.entity.MealPlan;
import com.mealmap.entity.MealPlanEntry;
import com.mealmap.entity.Recipe;
import com.mealmap.exception.MealPlanEntryNotFoundException;
import com.mealmap.repository.MealPlanEntryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MealPlanEntryControllerTest {

    @Mock
    private MealPlanEntryRepository mealPlanEntryRepository;

    @InjectMocks
    private MealPlanEntryController mealPlanEntryController;

    private MealPlanEntry entry(Long id, MealPlan mealPlan, Recipe recipe) {
        MealPlanEntry entry = new MealPlanEntry();
        setField(entry, "entryId", id);
        entry.setMealPlan(mealPlan);
        entry.setRecipe(recipe);
        return entry;
    }

    @Test
    void allReturnsAllEntries() {
        MealPlanEntry e = entry(1L, null, null);
        when(mealPlanEntryRepository.findAll()).thenReturn(List.of(e));

        List<MealPlanEntry> result = mealPlanEntryController.all();

        assertEquals(1, result.size());
    }

    @Test
    void allReturnsEmptyListWhenNoEntries() {
        when(mealPlanEntryRepository.findAll()).thenReturn(List.of());

        List<MealPlanEntry> result = mealPlanEntryController.all();

        assertTrue(result.isEmpty());
    }

    @Test
    void newMealPlanEntrySavesAndReturnsEntity() {
        MealPlan mealPlan = new MealPlan();
        Recipe recipe = new Recipe();
        MealPlanEntry entry = entry(null, mealPlan, recipe);

        when(mealPlanEntryRepository.save(entry)).thenReturn(entry);

        MealPlanEntry result = mealPlanEntryController.newMealPlanEntry(entry);

        assertEquals(mealPlan, result.getMealPlan());
        assertEquals(recipe, result.getRecipe());
        verify(mealPlanEntryRepository).save(entry);
    }

    @Test
    void oneReturnsEntryById() {
        MealPlanEntry e = entry(1L, null, null);
        when(mealPlanEntryRepository.findById(1L)).thenReturn(Optional.of(e));

        MealPlanEntry result = mealPlanEntryController.one(1L);

        assertNotNull(result);
    }

    @Test
    void oneThrowsWhenNotFound() {
        when(mealPlanEntryRepository.findById(99L)).thenReturn(
            Optional.empty()
        );

        assertThrows(MealPlanEntryNotFoundException.class, () ->
            mealPlanEntryController.one(99L)
        );
    }

    @Test
    void replaceMealPlanEntryUpdatesExistingEntry() {
        MealPlan mealPlan = new MealPlan();
        Recipe recipe = new Recipe();

        MealPlanEntry existing = entry(1L, null, null);
        MealPlanEntry update = entry(null, mealPlan, recipe);

        when(mealPlanEntryRepository.findById(1L)).thenReturn(
            Optional.of(existing)
        );
        when(mealPlanEntryRepository.save(any(MealPlanEntry.class))).thenAnswer(
            i -> i.getArgument(0)
        );

        MealPlanEntry result = mealPlanEntryController.replaceMealPlanEntry(
            update,
            1L
        );

        assertEquals(mealPlan, result.getMealPlan());
        assertEquals(recipe, result.getRecipe());
        verify(mealPlanEntryRepository).save(existing);
    }

    @Test
    void replaceMealPlanEntryCreatesNewWhenNotFound() {
        MealPlanEntry update = entry(null, new MealPlan(), new Recipe());

        when(mealPlanEntryRepository.findById(99L)).thenReturn(
            Optional.empty()
        );
        when(mealPlanEntryRepository.save(any(MealPlanEntry.class))).thenAnswer(
            i -> i.getArgument(0)
        );

        MealPlanEntry result = mealPlanEntryController.replaceMealPlanEntry(
            update,
            99L
        );

        assertNotNull(result);
        verify(mealPlanEntryRepository).save(update);
    }

    @Test
    void deleteMealPlanEntryDeletesById() {
        mealPlanEntryController.deleteMealPlanEntry(1L);
        verify(mealPlanEntryRepository).deleteById(1L);
    }
}
