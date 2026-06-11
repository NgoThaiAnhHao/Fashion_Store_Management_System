package com.student.fashion_store_management_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    private static final String UPLOAD_DIR = "uploads";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory(UPLOAD_DIR, registry);
    }

    private void exposeDirectory(String uploadDir, ResourceHandlerRegistry registry) {
        // Get path: /uploads
        Path path = Paths.get(uploadDir);

        // Sign url pattern /uploads/**
        registry.addResourceHandler("/" + uploadDir + "/**")

        // Declare position of file
        .addResourceLocations("file:" + path.toAbsolutePath() + "/");
    }


}
