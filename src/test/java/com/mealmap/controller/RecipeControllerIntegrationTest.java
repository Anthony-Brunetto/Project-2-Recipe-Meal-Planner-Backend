package com.mealmap.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mealmap.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class RecipeControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

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
