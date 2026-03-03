package com.example.demo.hello.entity;

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

    @Override
    public String toString() {
        return (
            "MealPlanEntry{" +
            "entryId=" +
            entryId +
            ", mealPlanId=" +
            (mealPlan != null ? mealPlan.getMealPlanId() : null) +
            ", recipeId=" +
            (recipe != null ? recipe.getRecipeId() : null) +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealPlanEntry)) return false;
        MealPlanEntry that = (MealPlanEntry) o;
        return entryId != null && entryId.equals(that.entryId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
