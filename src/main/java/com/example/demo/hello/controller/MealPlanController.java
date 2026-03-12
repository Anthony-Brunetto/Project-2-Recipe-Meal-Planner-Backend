package com.example.demo.hello.controller;

import com.example.demo.hello.entity.MealPlan;
import com.example.demo.hello.exception.MealPlanNotFoundException;
import com.example.demo.hello.repository.MealPlanRepository;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MealPlanController {

    private final MealPlanRepository mealPlanRepository;

    public MealPlanController(MealPlanRepository mealPlanRepository) {
        this.mealPlanRepository = mealPlanRepository;
    }

    @GetMapping("/api/meal-plans")
    List<MealPlan> all() {
        return mealPlanRepository.findAll();
    }

    @PostMapping("/api/meal-plans")
    MealPlan newMealPlan(@RequestBody MealPlan newMealPlan) {
        return mealPlanRepository.save(newMealPlan);
    }

    @GetMapping("/api/meal-plans/{mealPlanId}")
    MealPlan one(@PathVariable Long mealPlanId) {
        return mealPlanRepository
            .findById(mealPlanId)
            .orElseThrow(() -> new MealPlanNotFoundException(mealPlanId));
    }

    @PutMapping("/api/meal-plans/{mealPlanId}")
    MealPlan replaceMealPlan(
        @RequestBody MealPlan newMealPlan,
        @PathVariable Long mealPlanId
    ) {
        return mealPlanRepository
            .findById(mealPlanId)
            .map(mealPlan -> {
                mealPlan.setUser(newMealPlan.getUser());
                mealPlan.setDayOfWeek(newMealPlan.getDayOfWeek());
                mealPlan.setPartOfDay(newMealPlan.getPartOfDay());
                mealPlan.setMealPlanEntries(newMealPlan.getMealPlanEntries());
                return mealPlanRepository.save(mealPlan);
            })
            .orElseGet(() -> mealPlanRepository.save(newMealPlan));
    }

    @DeleteMapping("/api/meal-plans/{mealPlanId}")
    void deleteMealPlan(@PathVariable Long mealPlanId) {
        mealPlanRepository.deleteById(mealPlanId);
    }
}
