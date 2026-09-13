package com.cicd.compass.listener;

import java.lang.management.ManagementFactory;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Logs application startup information when the application is ready.
 */
@Component
public final class StartupLoggingListener {

    /**
     * Number of milliseconds in one second.
     */
    private static final double MILLISECONDS_TO_SECONDS = 1000.0;

    /**
     * Number of bytes in one kilobyte.
     */
    private static final int BYTES_TO_KB = 1024;

    /**
     * Number of kilobytes in one megabyte.
     */
    private static final int KB_TO_MB = 1024;

    /**
     * Logs application startup details after the application is ready.
     *
     * @param event application ready event
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onReady(final ApplicationReadyEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();

        String port = env.getProperty(
                "local.server.port",
                env.getProperty("server.port", "8080")
        );

        String appName = env.getProperty(
                "app.name",
                "cicd-compass-app"
        );

        String activeEnv = env.getProperty(
                "app.environment",
                "local"
        );

        double bootDurationSec = ManagementFactory
                .getRuntimeMXBean()
                .getUptime()
                / MILLISECONDS_TO_SECONDS;

        long totalMemoryMb = Runtime.getRuntime()
                .totalMemory()
                / BYTES_TO_KB
                / KB_TO_MB;

        System.out.println(
                "\n============================================================"
                        + "======================"
        );

        System.out.println(
                "[BOOT 1/5] Application : "
                        + appName
                        + " (Env: "
                        + activeEnv
                        + ")"
        );

        System.out.println(
                "[BOOT 2/5] Status      : READY & LISTENING on port "
                        + port
        );

        System.out.println(
                "[BOOT 3/5] Performance : Initialized in "
                        + String.format(
                                "%.2f",
                                bootDurationSec
                        )
                        + "s (Memory: "
                        + totalMemoryMb
                        + "MB)"
        );

        System.out.println(
                "[BOOT 4/5] Probes      : Health: http://localhost:"
                        + port
                        + "/api/health | Ready: http://localhost:"
                        + port
                        + "/api/ready"
        );

        System.out.println(
                "[BOOT 5/5] UI View     : Interactive DevSecOps Guide live "
                        + "at http://localhost:"
                        + port
                        + "/"
        );

        System.out.println(
                "=============================================================="
                        + "==========================\n"
        );
    }
}
