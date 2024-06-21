package com.example.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api/auth/**")
                .allowedOrigins("https://dogosydungs.azurewebsites.net/")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}