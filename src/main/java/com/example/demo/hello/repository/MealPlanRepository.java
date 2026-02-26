package com.example.demo.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.hello.entity.MealPlan;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
}
