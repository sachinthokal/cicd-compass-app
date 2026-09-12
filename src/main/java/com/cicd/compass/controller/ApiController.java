package com.cicd.compass.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Value("${app.name:cicd-compass-app}")
    private String appName;

    @Value("${app.version:v1.0.0}")
    private String appVersion;

    @Value("${app.environment:local}")
    private String environment;

    @Value("${app.region:ap-south-1}")
    private String region;

    @Value("${app.buildNumber:local-dev}")
    private String buildNumber;

    @GetMapping("/health")
    public ResponseEntity health() {
        Map response = new LinkedHashMap();
        response.put("status", "UP");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ready")
    public ResponseEntity ready() {
        Map response = new LinkedHashMap();
        response.put("status", "READY");
        response.put("service", appName);
        response.put("acceptingTraffic", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details")
    public ResponseEntity details() {
        Map details = new LinkedHashMap();
        details.put("application", appName);
        details.put("version", appVersion);
        details.put("environment", environment);
        details.put("region", region);
        details.put("buildNumber", buildNumber);
        details.put("javaVersion", System.getProperty("java.version"));
        details.put("jvmUptimeSeconds", ManagementFactory.getRuntimeMXBean().getUptime() / 1000);
        
        long freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long totalMem = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        details.put("memoryUsageMb", (totalMem - freeMem) + "MB / " + totalMem + "MB");
        
        return ResponseEntity.ok(details);
    }
}
