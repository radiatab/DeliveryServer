package fr.TabetSaidi.tp1Backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/deliveries/*")
                .allowedOrigins("http://127.0.0.1:4200") // Remplacez par l'URL de votre frontend Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")
                .allowedHeaders("Origin", "Content-Type", "Accept");
        registry.addMapping("/deliverypersons/**")
                .allowedOrigins("http://127.0.0.1:4200") // Remplacez par l'URL de votre frontend Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")
                .allowedHeaders("Origin", "Content-Type", "Accept");
        registry.addMapping("/tours/**")
                .allowedOrigins("http://127.0.0.1:4200") // Remplacez par l'URL de votre frontend Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")
                .allowedHeaders("Origin", "Content-Type", "Accept");
    }
}

