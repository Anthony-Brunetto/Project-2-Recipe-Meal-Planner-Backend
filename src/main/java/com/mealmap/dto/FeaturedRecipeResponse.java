package com.mealmap.dto;

public record FeaturedRecipeResponse(
    Long id,
    String name,
    Integer cookTimeMinutes,
    String category,
    String cuisine,
    String difficulty,
    String imageUrl
) {}
