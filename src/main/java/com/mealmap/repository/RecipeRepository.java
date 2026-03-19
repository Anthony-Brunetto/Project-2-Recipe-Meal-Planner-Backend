package com.mealmap.repository;

import com.mealmap.entity.Recipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByUser_UserId(Long userId);
}
