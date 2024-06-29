package com.example.demo.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){


        registry.addMapping("/**")
                .allowedOrigins("https://dogosydungs.azurewebsites.net", "http://localhost:5173", "https://dogosydungfe.azurewebsites.net") //http://localhost:5173
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .info(new Info().title("Example API").version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}