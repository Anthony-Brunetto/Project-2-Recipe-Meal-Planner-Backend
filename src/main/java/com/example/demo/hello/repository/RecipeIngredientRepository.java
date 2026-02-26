package com.example.demo.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.hello.entity.RecipeIngredient;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}
