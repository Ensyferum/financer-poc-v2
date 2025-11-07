package com.financer.eureka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * Configuration class for Eureka Client with custom settings.
 * Provides logging and monitoring for service registration.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "eureka.client.enabled", havingValue = "true", matchIfMissing = true)
public class EurekaClientConfig {

    @Value("${spring.application.name:unknown-service}")
    private String applicationName;

    @Value("${eureka.client.service-url.defaultZone:http://localhost:8761/eureka}")
    private String eurekaServerUrl;

    @Value("${eureka.instance.prefer-ip-address:false}")
    private boolean preferIpAddress;

    @PostConstruct
    public void init() {
        log.info("=".repeat(60));
        log.info("Eureka Client Configuration Initialized");
        log.info("=".repeat(60));
        log.info("Application Name: {}", applicationName);
        log.info("Eureka Server URL: {}", eurekaServerUrl);
        log.info("Prefer IP Address: {}", preferIpAddress);
        log.info("=".repeat(60));
    }

    /**
     * Logs all registered service instances for debugging.
     * 
     * @param discoveryClient Spring Cloud Discovery Client
     */
    public void logRegisteredServices(DiscoveryClient discoveryClient) {
        if (discoveryClient == null) {
            log.warn("DiscoveryClient is not available");
            return;
        }

        try {
            List<String> services = discoveryClient.getServices();
            log.info("Total registered services: {}", services.size());
            
            for (String service : services) {
                List<ServiceInstance> instances = discoveryClient.getInstances(service);
                log.info("Service: {} - Instances: {}", service, instances.size());
                
                for (ServiceInstance instance : instances) {
                    log.debug("  Instance ID: {}, Host: {}, Port: {}, URI: {}", 
                        instance.getInstanceId(),
                        instance.getHost(),
                        instance.getPort(),
                        instance.getUri());
                }
            }
        } catch (Exception e) {
            log.error("Error logging registered services", e);
        }
    }
}
