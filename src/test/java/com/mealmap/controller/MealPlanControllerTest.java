package com.mealmap.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.mealmap.entity.MealPlan;
import com.mealmap.entity.MealPlanEntry;
import com.mealmap.entity.User;
import com.mealmap.exception.MealPlanNotFoundException;
import com.mealmap.repository.MealPlanRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MealPlanControllerTest {

    @Mock
    private MealPlanRepository mealPlanRepository;

    @InjectMocks
    private MealPlanController mealPlanController;

    private MealPlan mealPlan(Long id, String dayOfWeek, String partOfDay) {
        MealPlan mealPlan = new MealPlan();
        setField(mealPlan, "mealPlanId", id);
        mealPlan.setDayOfWeek(dayOfWeek);
        mealPlan.setPartOfDay(partOfDay);
        return mealPlan;
    }

    @Test
    void allReturnsAllMealPlans() {
        MealPlan mp = mealPlan(1L, "Monday", "Dinner");
        when(mealPlanRepository.findAll()).thenReturn(List.of(mp));

        List<MealPlan> result = mealPlanController.all();

        assertEquals(1, result.size());
        assertEquals("Monday", result.getFirst().getDayOfWeek());
    }

    @Test
    void allReturnsEmptyListWhenNoMealPlans() {
        when(mealPlanRepository.findAll()).thenReturn(List.of());

        List<MealPlan> result = mealPlanController.all();

        assertTrue(result.isEmpty());
    }

    @Test
    void newMealPlanSavesAndReturnsEntity() {
        MealPlan mp = mealPlan(null, "Tuesday", "Lunch");
        when(mealPlanRepository.save(mp)).thenReturn(mp);

        MealPlan result = mealPlanController.newMealPlan(mp);

        assertEquals("Tuesday", result.getDayOfWeek());
        assertEquals("Lunch", result.getPartOfDay());
        verify(mealPlanRepository).save(mp);
    }

    @Test
    void oneReturnsMealPlanById() {
        MealPlan mp = mealPlan(1L, "Monday", "Dinner");
        when(mealPlanRepository.findById(1L)).thenReturn(Optional.of(mp));

        MealPlan result = mealPlanController.one(1L);

        assertEquals("Monday", result.getDayOfWeek());
    }

    @Test
    void oneThrowsWhenNotFound() {
        when(mealPlanRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MealPlanNotFoundException.class, () ->
            mealPlanController.one(99L)
        );
    }

    @Test
    void replaceMealPlanUpdatesExistingMealPlan() {
        User user = new User("supabase-123", "john@example.com", "john");
        MealPlanEntry entry = new MealPlanEntry();

        MealPlan existing = mealPlan(1L, "Monday", "Dinner");
        MealPlan update = mealPlan(null, "Wednesday", "Breakfast");
        update.setUser(user);
        update.setMealPlanEntries(List.of(entry));

        when(mealPlanRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(mealPlanRepository.save(any(MealPlan.class))).thenAnswer(i ->
            i.getArgument(0)
        );

        MealPlan result = mealPlanController.replaceMealPlan(update, 1L);

        assertEquals("Wednesday", result.getDayOfWeek());
        assertEquals("Breakfast", result.getPartOfDay());
        assertEquals(user, result.getUser());
        verify(mealPlanRepository).save(existing);
    }

    @Test
    void replaceMealPlanCreatesNewWhenNotFound() {
        MealPlan update = mealPlan(null, "Friday", "Lunch");

        when(mealPlanRepository.findById(99L)).thenReturn(Optional.empty());
        when(mealPlanRepository.save(any(MealPlan.class))).thenAnswer(i ->
            i.getArgument(0)
        );

        MealPlan result = mealPlanController.replaceMealPlan(update, 99L);

        assertEquals("Friday", result.getDayOfWeek());
        verify(mealPlanRepository).save(update);
    }

    @Test
    void deleteMealPlanDeletesById() {
        mealPlanController.deleteMealPlan(1L);
        verify(mealPlanRepository).deleteById(1L);
    }
}
