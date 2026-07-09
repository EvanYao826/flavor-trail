package com.flavor.trail.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("寻味中国 API")
                        .description("寻味中国 - 中国美食探索平台 API文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Flavor Trail")
                                .email("support@flavortrail.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("本地开发环境")
                ));
    }
}