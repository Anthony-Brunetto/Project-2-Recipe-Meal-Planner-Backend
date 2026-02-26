package com.example.demo.hello.repository;

import com.example.demo.hello.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {}
