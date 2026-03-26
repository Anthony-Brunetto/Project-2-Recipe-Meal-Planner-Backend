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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class MealPlanControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void getAllMealPlansReturns200() throws Exception {
        mockMvc
            .perform(
                get("/api/meal-plans").contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getMealPlanByIdReturns404WhenNotFound() throws Exception {
        mockMvc
            .perform(
                get("/api/meal-plans/99999").contentType(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void createMealPlanReturns200() throws Exception {
        String mealPlanJson = """
            {
                "dayOfWeek": "Monday",
                "partOfDay": "Dinner"
            }
            """;

        mockMvc
            .perform(
                post("/api/meal-plans")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mealPlanJson)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.dayOfWeek").value("Monday"))
            .andExpect(jsonPath("$.partOfDay").value("Dinner"));
    }

    @Test
    void updateMealPlanReturns200() throws Exception {
        String createJson = """
            {
                "dayOfWeek": "Tuesday",
                "partOfDay": "Lunch"
            }
            """;

        MvcResult result = mockMvc
            .perform(
                post("/api/meal-plans")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createJson)
            )
            .andExpect(status().isOk())
            .andReturn();

        Long mealPlanId = objectMapper
            .readTree(result.getResponse().getContentAsString())
            .get("mealPlanId")
            .asLong();

        String updateJson = """
            {
                "dayOfWeek": "Wednesday",
                "partOfDay": "Breakfast"
            }
            """;

        mockMvc
            .perform(
                put("/api/meal-plans/" + mealPlanId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateJson)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.dayOfWeek").value("Wednesday"))
            .andExpect(jsonPath("$.partOfDay").value("Breakfast"));
    }

    @Test
    void deleteMealPlanReturns204WhenNotFound() throws Exception {
        mockMvc
            .perform(
                delete("/api/meal-plans/99999").contentType(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(status().isOk());
    }
}
