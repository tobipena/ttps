package org.example.ttps.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ttps-api",
                version = "v1.0.0"
        )
)
public class SwaggerConfig {
    @Bean
    GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("api")
                .packagesToScan("org.example.ttps.controller")
                .build();
    }
}
