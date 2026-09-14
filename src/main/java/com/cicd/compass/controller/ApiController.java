package com.cicd.compass.controller;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for application health, readiness and details endpoints.
 */
@RestController
@RequestMapping("/api")
public final class ApiController {

    /**
     * Number of bytes in one kilobyte.
     */
    private static final int BYTES_TO_KB = 1024;

    /**
     * Number of kilobytes in one megabyte.
     */
    private static final int KB_TO_MB = 1024;

    /**
     * Number of milliseconds in one second.
     */
    private static final int MILLISECONDS_TO_SECONDS = 1000;

    /**
     * Application name.
     */
    @Value("${app.name}")
    private String appName;

    /**
     * Application version.
     */
    @Value("${app.version}")
    private String appVersion;

    /**
     * Application environment.
     */
    @Value("${app.environment}")
    private String environment;

    /**
     * Application region.
     */
    @Value("${app.region}")
    private String region;

    /**
     * Application build number.
     */
    @Value("${app.buildNumber}")
    private String buildNumber;

    /**
     * Returns the application health status.
     *
     * @return health status response
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", "UP");
        response.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Returns the application readiness status.
     *
     * @return readiness status response
     */
    @GetMapping("/ready")
    public ResponseEntity<Map<String, Object>> ready() {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", "READY");
        response.put("service", appName);
        response.put("acceptingTraffic", true);

        return ResponseEntity.ok(response);
    }

    /**
     * Returns application and JVM runtime details.
     *
     * @return application details response
     */
    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> details() {
        Map<String, Object> details = new LinkedHashMap<>();

        details.put("application", appName);
        details.put("version", appVersion);
        details.put("environment", environment);
        details.put("region", region);
        details.put("buildNumber", buildNumber);
        details.put("javaVersion", System.getProperty("java.version"));

        long uptimeMillis = ManagementFactory
                .getRuntimeMXBean()
                .getUptime();

        long uptimeSeconds = uptimeMillis / MILLISECONDS_TO_SECONDS;

        details.put("jvmUptimeSeconds", uptimeSeconds);

        Runtime runtime = Runtime.getRuntime();

        long freeMemoryBytes = runtime.freeMemory();
        long totalMemoryBytes = runtime.totalMemory();

        long freeMemoryMb = freeMemoryBytes
                / BYTES_TO_KB
                / KB_TO_MB;

        long totalMemoryMb = totalMemoryBytes
                / BYTES_TO_KB
                / KB_TO_MB;

        long usedMemoryMb = totalMemoryMb - freeMemoryMb;

        details.put(
                "memoryUsageMb",
                usedMemoryMb + "MB / " + totalMemoryMb + "MB"
        );

        return ResponseEntity.ok(details);
    }
}
