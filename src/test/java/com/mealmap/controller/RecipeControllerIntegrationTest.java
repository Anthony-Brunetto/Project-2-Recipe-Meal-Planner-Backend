package com.mealmap.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mealmap.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class RecipeControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAllRecipesReturns200() throws Exception {
        mockMvc
            .perform(
                get("/api/recipes").contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getRecipeByIdReturns404WhenNotFound() throws Exception {
        mockMvc
            .perform(
                get("/api/recipes/99999").contentType(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void createRecipeReturns200() throws Exception {
        String recipeJson = """
            {
                "name": "Test Recipe",
                "description": "A test",
                "instructions": "Do the thing"
            }
            """;

        mockMvc
            .perform(
                post("/api/recipes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(recipeJson)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Recipe"));
    }

    @Test
    void deleteRecipeReturns404WhenNotFound() throws Exception {
        mockMvc
            .perform(
                delete("/api/recipes/99999").contentType(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(status().isNotFound());
    }
}
