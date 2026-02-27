package com.example.demo.hello.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;

    private String ingredientName;
    private String description;
    private String unit;
    private Integer calories;

    public Ingredient() {}

    public Long getIngredientId() {
        return ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return (
            "Ingredient{" +
            "ingredientId=" +
            ingredientId +
            ", ingredientName='" +
            ingredientName +
            '\'' +
            ", description='" +
            description +
            '\'' +
            ", unit='" +
            unit +
            '\'' +
            ", calories=" +
            calories +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return ingredientId != null && ingredientId.equals(that.ingredientId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
