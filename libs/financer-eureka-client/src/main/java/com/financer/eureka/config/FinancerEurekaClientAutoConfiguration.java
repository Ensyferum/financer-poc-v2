package com.financer.eureka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Auto-configuration class for Financer Eureka Client Library.
 * Enables Eureka service discovery for all microservices.
 * 
 * Note: @EnableEurekaClient is no longer required in Spring Cloud 2023.0.0+
 * Service registration is now enabled automatically when eureka-client is on classpath.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Slf4j
@AutoConfiguration
@ComponentScan(basePackages = "com.financer.eureka")
public class FinancerEurekaClientAutoConfiguration {
    
    public FinancerEurekaClientAutoConfiguration() {
        log.info("Financer Eureka Client Auto-Configuration initialized");
    }
}
