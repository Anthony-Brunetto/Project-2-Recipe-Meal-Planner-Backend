package com.mealmap.repository;

import com.mealmap.entity.MealPlanEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealPlanEntryRepository
    extends JpaRepository<MealPlanEntry, Long> {}
