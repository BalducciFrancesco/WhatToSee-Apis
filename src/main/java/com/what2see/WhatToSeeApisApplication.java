package com.what2see;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main class for the Spring Boot application.
 * @see SpringBootApplication
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 *
 */
@SpringBootApplication
public class WhatToSeeApisApplication {

    /**
     * Main method for the Spring Boot application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(WhatToSeeApisApplication.class, args);
    }

    /**
     * Configures the CORS policy for the Spring Boot application.<br>
     * This allows the frontend to access the backend APIs from any origin and with any method.
     * @return the CORS policy configuration
     * @see WebMvcConfigurer
     * @see CorsRegistry
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
            }
        };
    }

}
