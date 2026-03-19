package com.mealmap.controller;

import com.mealmap.entity.MealPlanEntry;
import com.mealmap.exception.MealPlanEntryNotFoundException;
import com.mealmap.repository.MealPlanEntryRepository;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MealPlanEntryController {

    private final MealPlanEntryRepository mealPlanEntryRepository;

    public MealPlanEntryController(
        MealPlanEntryRepository mealPlanEntryRepository
    ) {
        this.mealPlanEntryRepository = mealPlanEntryRepository;
    }

    @GetMapping("/api/meal-plan-entries")
    List<MealPlanEntry> all() {
        return mealPlanEntryRepository.findAll();
    }

    @PostMapping("/api/meal-plan-entries")
    MealPlanEntry newMealPlanEntry(
        @RequestBody MealPlanEntry newMealPlanEntry
    ) {
        return mealPlanEntryRepository.save(newMealPlanEntry);
    }

    @GetMapping("/api/meal-plan-entries/{entryId}")
    MealPlanEntry one(@PathVariable Long entryId) {
        return mealPlanEntryRepository
            .findById(entryId)
            .orElseThrow(() -> new MealPlanEntryNotFoundException(entryId));
    }

    @PutMapping("/api/meal-plan-entries/{entryId}")
    MealPlanEntry replaceMealPlanEntry(
        @RequestBody MealPlanEntry newMealPlanEntry,
        @PathVariable Long entryId
    ) {
        return mealPlanEntryRepository
            .findById(entryId)
            .map(mealPlanEntry -> {
                mealPlanEntry.setMealPlan(newMealPlanEntry.getMealPlan());
                mealPlanEntry.setRecipe(newMealPlanEntry.getRecipe());
                return mealPlanEntryRepository.save(mealPlanEntry);
            })
            .orElseGet(() -> mealPlanEntryRepository.save(newMealPlanEntry));
    }

    @DeleteMapping("/api/meal-plan-entries/{entryId}")
    void deleteMealPlanEntry(@PathVariable Long entryId) {
        mealPlanEntryRepository.deleteById(entryId);
    }
}
