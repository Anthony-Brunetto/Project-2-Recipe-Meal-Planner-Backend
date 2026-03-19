package com.mealmap.repository;

import com.mealmap.entity.RecipeIngredient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository
    extends JpaRepository<RecipeIngredient, Long>
{
    List<RecipeIngredient> findByRecipe_RecipeId(Long recipeId);
}
