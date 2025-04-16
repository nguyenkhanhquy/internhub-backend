package com.internhub.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Intern Hub API Documentation", version = "0.1.0"
        ),
        servers = {
                @Server(url = "/api/v1", description = "Primary API server")
        }
)
public class SwaggerConfig {

}
