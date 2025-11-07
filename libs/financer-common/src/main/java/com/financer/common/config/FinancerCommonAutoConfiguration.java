package com.financer.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration class for Financer Common Library.
 * This enables all common features when the library is included in a project.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@AutoConfiguration
@ComponentScan(basePackages = "com.financer.common")
public class FinancerCommonAutoConfiguration {
    
    public FinancerCommonAutoConfiguration() {
        // Constructor for Spring Boot auto-configuration
    }
}
