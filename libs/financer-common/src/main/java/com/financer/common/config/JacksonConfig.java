package com.financer.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson configuration for consistent JSON serialization/deserialization.
 * Configures Java 8 date/time support and formatting rules.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates a configured ObjectMapper bean with:
     * - Java 8 Date/Time support
     * - ISO-8601 date formatting
     * - Pretty printing disabled (for production)
     * - Unknown properties ignored
     * - Null values included
     *
     * @return Configured ObjectMapper
     */
    @Bean
    @Primary
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Register Java 8 Date/Time module
        mapper.registerModule(new JavaTimeModule());
        
        // Disable timestamp serialization (use ISO-8601 instead)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Disable pretty printing (production)
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        
        // Ignore unknown properties during deserialization
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // Include null values (can be changed per use case)
        mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS);
        
        return mapper;
    }
}
