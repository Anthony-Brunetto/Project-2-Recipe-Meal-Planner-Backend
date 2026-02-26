package com.example.demo.hello.entity;

import com.example.demo.hello.entity.MealPlan;
import com.example.demo.hello.entity.Recipe;
import jakarta.persistence.*;

@Entity
@Table(name = "mealPlanEntry")
public class MealPlanEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entryId;

    @ManyToOne
    @JoinColumn(name = "mealPlanId")
    private MealPlan mealPlan;

    @ManyToOne
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    public MealPlanEntry() {}

    public Long getEntryId() {
        return entryId;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    public void setMealPlan(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
