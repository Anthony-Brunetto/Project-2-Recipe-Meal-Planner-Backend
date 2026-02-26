package com.example.demo.hello.entity;

import com.example.demo.hello.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String name;
    private String description;
    private String instructions;
    private String originalUser;

    public Recipe() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getOriginalUser() {
        return originalUser;
    }

    public void setOriginalUser(String originalUser) {
        this.originalUser = originalUser;
    }
}
