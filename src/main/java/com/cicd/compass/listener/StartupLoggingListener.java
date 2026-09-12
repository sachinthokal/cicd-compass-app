package com.cicd.compass.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

@Component
public class StartupLoggingListener {

    @EventListener(ApplicationReadyEvent.class)
    public void onReady(ApplicationReadyEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();
        String port = env.getProperty("local.server.port", env.getProperty("server.port", "8080"));
        String appName = env.getProperty("app.name", "cicd-compass-app");
        String activeEnv = env.getProperty("app.environment", "local");
        
        double bootDurationSec = ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0;
        long totalMemoryMb = Runtime.getRuntime().totalMemory() / (1024 * 1024);

        System.out.println("\n================================================================================");
        System.out.println("[BOOT 1/5] Application : " + appName + " (Env: " + activeEnv + ")");
        System.out.println("[BOOT 2/5] Status      : READY & LISTENING on port " + port);
        System.out.println("[BOOT 3/5] Performance : Initialized in " + String.format("%.2f", bootDurationSec) + "s (Memory: " + totalMemoryMb + "MB)");
        System.out.println("[BOOT 4/5] Probes      : Health: http://localhost:" + port + "/api/health | Ready: http://localhost:" + port + "/api/ready");
        System.out.println("[BOOT 5/5] UI View     : Interactive DevSecOps Guide live at http://localhost:" + port + "/");
        System.out.println("================================================================================\n");
    }
}
