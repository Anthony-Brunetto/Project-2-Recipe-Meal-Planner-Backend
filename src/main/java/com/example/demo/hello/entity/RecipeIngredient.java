package com.example.demo.hello.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recipeIngredient")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeIngredientId;

    @ManyToOne
    @JoinColumn(name = "ingredientId")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    private Integer quantity;
    private String unit;

    public RecipeIngredient() {}

    public Long getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return (
            "RecipeIngredient{" +
            "recipeIngredientId=" +
            recipeIngredientId +
            ", ingredientId=" +
            (ingredient != null ? ingredient.getIngredientId() : null) +
            ", recipeId=" +
            (recipe != null ? recipe.getRecipeId() : null) +
            ", quantity=" +
            quantity +
            ", unit='" +
            unit +
            '\'' +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeIngredient)) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return (
            recipeIngredientId != null &&
            recipeIngredientId.equals(that.recipeIngredientId)
        );
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
