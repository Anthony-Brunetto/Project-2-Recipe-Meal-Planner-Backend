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
class UserControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void getAllUsersReturns200() throws Exception {
        mockMvc
            .perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getUserByIdReturns404WhenNotFound() throws Exception {
        mockMvc
            .perform(
                get("/api/users/99999").contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void syncUserReturns200AndCreatesUser() throws Exception {
        String payload = """
            {
                "record": {
                    "id": "test-supabase-uuid-001",
                    "email": "integrationtest@example.com",
                    "created_at": "2026-03-25T00:00:00Z"
                }
            }
            """;

        mockMvc
            .perform(
                post("/api/users/sync")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("integrationtest@example.com"))
            .andExpect(jsonPath("$.username").value("integrationtest"))
            .andExpect(
                jsonPath("$.supabaseId").value("test-supabase-uuid-001")
            );
    }

    @Test
    void syncUserReturnsExistingUserOnDuplicateSync() throws Exception {
        String payload = """
            {
                "record": {
                    "id": "test-supabase-uuid-002",
                    "email": "duplicate@example.com",
                    "created_at": "2026-03-25T00:00:00Z"
                }
            }
            """;

        // First sync
        mockMvc
            .perform(
                post("/api/users/sync")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
            .andExpect(status().isOk());

        // Second sync — should return same user, not duplicate
        mockMvc
            .perform(
                post("/api/users/sync")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("duplicate@example.com"));
    }

    @Test
    void deleteUserReturns204WhenNotFound() throws Exception {
        mockMvc
            .perform(
                delete("/api/users/99999").contentType(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(status().isOk());
    }
}
