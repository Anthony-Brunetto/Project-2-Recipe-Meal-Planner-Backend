package com.example.demo.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.hello.entity.MealPlanEntry;

public interface MealPlanEntryRepository extends JpaRepository<MealPlanEntry, Long> {
}
