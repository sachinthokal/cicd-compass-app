package com.cicd.compass.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders
        .get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers
        .jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers
        .status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for the ApiController REST endpoints.
 */
@WebMvcTest(ApiController.class)
@TestPropertySource(properties = {
        "app.name=cicd-compass-app",
        "app.version=v1.0.0",
        "app.environment=test",
        "app.region=ap-south-1",
        "app.buildNumber=test"
})
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests the health endpoint.
     *
     * @throws Exception when the request fails
     */
    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    /**
     * Tests the readiness endpoint.
     *
     * @throws Exception when the request fails
     */
    @Test
    void testReadyEndpoint() throws Exception {
        mockMvc.perform(get("/api/ready"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("READY"))
                .andExpect(jsonPath("$.service")
                        .value("cicd-compass-app"))
                .andExpect(jsonPath("$.acceptingTraffic").value(true));
    }

    /**
     * Tests the application details endpoint.
     *
     * @throws Exception when the request fails
     */
    @Test
    void testDetailsEndpoint() throws Exception {
        mockMvc.perform(get("/api/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.application")
                        .value("cicd-compass-app"))
                .andExpect(jsonPath("$.version")
                        .value("v1.0.0"))
                .andExpect(jsonPath("$.environment")
                        .value("test"))
                .andExpect(jsonPath("$.javaVersion").exists())
                .andExpect(jsonPath("$.memoryUsageMb").exists());
    }
}
