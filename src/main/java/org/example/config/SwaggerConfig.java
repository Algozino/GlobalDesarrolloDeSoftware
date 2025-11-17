package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Detector de mutantes API - MercadoLibre")
                        .version("1.0.0")
                        .description("API para detectar ADN mutante y proveer estadísticas, " + "basado en el desafío técnico de MercadoLibre."));
    }
}