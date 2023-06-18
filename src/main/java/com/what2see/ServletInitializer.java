package com.what2see;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Servlet initializer class for the Spring Boot application.
 * @see SpringBootServletInitializer
 * @see WhatToSeeApisApplication
 * @see org.springframework.boot.web.servlet.ServletContextInitializer
 * @see org.springframework.boot.web.servlet.support.SpringBootServletInitializer
 */
public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * Configures the application context for the Spring Boot application.
     * @param application a builder for the application context
     * @return the application builder
     * @see SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WhatToSeeApisApplication.class);
    }

}
