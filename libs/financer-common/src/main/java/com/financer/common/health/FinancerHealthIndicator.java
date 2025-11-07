package com.financer.common.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom health indicator for Financer applications.
 * Provides application health status and metadata.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Slf4j
@Component
public class FinancerHealthIndicator implements HealthIndicator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${spring.application.name:Financer Service}")
    private String applicationName;

    @Value("${spring.application.version:unknown}")
    private String applicationVersion;

    @Value("${financer.common.version:@project.version@}")
    private String commonLibVersion;

    @Override
    public Health health() {
        try {
            return Health.up()
                    .withDetail("status", "Application is running")
                    .withDetail("timestamp", LocalDateTime.now().format(FORMATTER))
                    .withDetail("service", applicationName)
                    .withDetail("version", applicationVersion)
                    .withDetail("financer-common-version", commonLibVersion)
                    .build();
        } catch (Exception e) {
            log.error("Health check failed", e);
            return Health.down()
                    .withDetail("status", "Application health check failed")
                    .withDetail("error", e.getMessage())
                    .withDetail("timestamp", LocalDateTime.now().format(FORMATTER))
                    .build();
        }
    }
}
