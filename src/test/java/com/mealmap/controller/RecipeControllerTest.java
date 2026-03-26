package com.mealmap.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealmap.dto.FeaturedRecipeResponse;
import com.mealmap.repository.RecipeRepository;
import com.mealmap.service.RecipeService;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class RecipeControllerTest {

    @Test
    void featuredTodayEndpointReturnsHomepageResponseShape() throws Exception {
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        RecipeService recipeService = mock(RecipeService.class);
        when(recipeService.getTodayFeaturedRecipes())
            .thenReturn(
                List.of(
                    new FeaturedRecipeResponse(
                        42L,
                        "Tomato Pasta",
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )
            );
        when(recipeService.getCacheTtl()).thenReturn(Duration.ofHours(5));

        MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new RecipeController(recipeRepository, recipeService))
            .build();

        mockMvc
            .perform(get("/api/recipes/featured/today"))
            .andExpect(status().isOk())
            .andExpect(header().string("Cache-Control", "max-age=18000, public"))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(42))
            .andExpect(jsonPath("$[0].name").value("Tomato Pasta"))
            .andExpect(jsonPath("$[0].cookTimeMinutes").value(nullValue()))
            .andExpect(jsonPath("$[0].category").value(nullValue()))
            .andExpect(jsonPath("$[0].cuisine").value(nullValue()))
            .andExpect(jsonPath("$[0].difficulty").value(nullValue()))
            .andExpect(jsonPath("$[0].imageUrl").value(nullValue()));
    }
}
