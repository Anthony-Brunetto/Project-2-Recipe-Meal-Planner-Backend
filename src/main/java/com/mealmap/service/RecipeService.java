package com.mealmap.service;

import com.mealmap.dto.FeaturedRecipeResponse;
import com.mealmap.entity.Recipe;
import com.mealmap.repository.RecipeRepository;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SplittableRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    static final int FEATURED_RECIPE_LIMIT = 6;

    private final RecipeRepository recipeRepository;
    private final Clock clock;
    private final ZoneId featuredZoneId;

    @Autowired
    public RecipeService(
        RecipeRepository recipeRepository,
        @Value("${app.featured-recipes.zone-id:UTC}") String featuredZoneId
    ) {
        this(
            recipeRepository,
            Clock.system(ZoneId.of(featuredZoneId)),
            ZoneId.of(featuredZoneId)
        );
    }

    RecipeService(
        RecipeRepository recipeRepository,
        Clock clock,
        ZoneId featuredZoneId
    ) {
        this.recipeRepository = recipeRepository;
        this.clock = clock;
        this.featuredZoneId = featuredZoneId;
    }

    public List<FeaturedRecipeResponse> getTodayFeaturedRecipes() {
        return getFeaturedRecipesForDate(LocalDate.now(clock));
    }

    public List<FeaturedRecipeResponse> getFeaturedRecipesForDate(LocalDate date) {
        List<Recipe> orderedRecipes = recipeRepository.findAll(
            Sort.by(Sort.Direction.ASC, "recipeId")
        );

        if (orderedRecipes.size() <= FEATURED_RECIPE_LIMIT) {
            return orderedRecipes.stream().map(this::toFeaturedRecipeResponse).toList();
        }

        List<Recipe> shuffledRecipes = new ArrayList<>(orderedRecipes);
        shuffleDeterministically(shuffledRecipes, date);

        return shuffledRecipes
            .stream()
            .limit(FEATURED_RECIPE_LIMIT)
            .map(this::toFeaturedRecipeResponse)
            .toList();
    }

    public Duration getCacheTtl() {
        LocalDate today = LocalDate.now(clock);
        return Duration.between(
            clock.instant(),
            today.plusDays(1).atStartOfDay(featuredZoneId).toInstant()
        );
    }

    private void shuffleDeterministically(List<Recipe> recipes, LocalDate date) {
        long seed = Objects.hash(date.toString(), "featured-recipes-v1");
        SplittableRandom random = new SplittableRandom(seed);

        for (int index = recipes.size() - 1; index > 0; index--) {
            int swapIndex = random.nextInt(index + 1);
            Recipe current = recipes.get(index);
            recipes.set(index, recipes.get(swapIndex));
            recipes.set(swapIndex, current);
        }
    }

    private FeaturedRecipeResponse toFeaturedRecipeResponse(Recipe recipe) {
        return new FeaturedRecipeResponse(
            recipe.getRecipeId(),
            recipe.getName(),
            null,
            null,
            null,
            null,
            null
        );
    }
}
