package com.example.demo.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.hello.entity.Recipes;

public interface RecipesRepository extends JpaRepository<Recipes, Long> {
}
