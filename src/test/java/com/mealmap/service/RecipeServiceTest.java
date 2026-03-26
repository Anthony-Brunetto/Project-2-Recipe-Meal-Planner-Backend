package com.mealmap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mealmap.dto.FeaturedRecipeResponse;
import com.mealmap.entity.Recipe;
import com.mealmap.repository.RecipeRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    private static final ZoneId FEATURED_ZONE = ZoneOffset.UTC;

    @Mock
    private RecipeRepository recipeRepository;

    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(
            Instant.parse("2026-03-25T12:00:00Z"),
            FEATURED_ZONE
        );
        recipeService = new RecipeService(recipeRepository, clock, FEATURED_ZONE);
    }

    @Test
    void sameDateReturnsSameResults() {
        when(recipeRepository.findAll(any(Sort.class))).thenReturn(createRecipes(10));

        List<FeaturedRecipeResponse> first = recipeService.getFeaturedRecipesForDate(
            LocalDate.of(2026, 3, 25)
        );
        List<FeaturedRecipeResponse> second = recipeService.getFeaturedRecipesForDate(
            LocalDate.of(2026, 3, 25)
        );

        assertThat(first).isEqualTo(second);
        assertThat(first).hasSize(6);
    }

    @Test
    void differentDatesCanReturnDifferentResults() {
        when(recipeRepository.findAll(any(Sort.class))).thenReturn(createRecipes(10));

        List<FeaturedRecipeResponse> today = recipeService.getFeaturedRecipesForDate(
            LocalDate.of(2026, 3, 25)
        );
        List<FeaturedRecipeResponse> tomorrow = recipeService.getFeaturedRecipesForDate(
            LocalDate.of(2026, 3, 26)
        );

        assertThat(today).isNotEqualTo(tomorrow);
    }

    @Test
    void fewerThanSixRecipesReturnsAllRecipes() {
        when(recipeRepository.findAll(any(Sort.class))).thenReturn(createRecipes(4));

        List<FeaturedRecipeResponse> featuredRecipes = recipeService.getTodayFeaturedRecipes();

        assertThat(featuredRecipes).hasSize(4);
        assertThat(featuredRecipes)
            .extracting(FeaturedRecipeResponse::id)
            .containsExactly(1L, 2L, 3L, 4L);
    }

    @Test
    void featuredRecipeResponseContainsHomepageFields() {
        when(recipeRepository.findAll(any(Sort.class))).thenReturn(createRecipes(1));

        FeaturedRecipeResponse featuredRecipe = recipeService
            .getTodayFeaturedRecipes()
            .getFirst();

        assertThat(featuredRecipe.id()).isEqualTo(1L);
        assertThat(featuredRecipe.name()).isEqualTo("Recipe 1");
        assertThat(featuredRecipe.cookTimeMinutes()).isNull();
        assertThat(featuredRecipe.category()).isNull();
        assertThat(featuredRecipe.cuisine()).isNull();
        assertThat(featuredRecipe.difficulty()).isNull();
        assertThat(featuredRecipe.imageUrl()).isNull();
    }

    private List<Recipe> createRecipes(int count) {
        return java.util.stream.LongStream
            .rangeClosed(1, count)
            .mapToObj(id -> createRecipe(id, "Recipe " + id))
            .toList();
    }

    private Recipe createRecipe(long id, String name) {
        Recipe recipe = new Recipe();
        ReflectionTestUtils.setField(recipe, "recipeId", id);
        recipe.setName(name);
        return recipe;
    }
}
