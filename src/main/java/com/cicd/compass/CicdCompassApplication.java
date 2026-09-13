package com.cicd.compass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the CICD Compass application.
 */
@SpringBootApplication
public class CicdCompassApplication {

    /**
     * Protected constructor required by Spring CGLIB.
     */
    protected CicdCompassApplication() {
        // Required for Spring configuration class enhancement.
    }

    /**
     * Starts the Spring Boot application.
     *
     * @param args application command-line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(CicdCompassApplication.class, args);
    }
}
