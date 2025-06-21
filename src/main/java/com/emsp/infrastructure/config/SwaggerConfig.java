package com.emsp.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("accounts")
                .pathsToMatch("/api/accounts/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi cardApi() {
        return GroupedOpenApi.builder()
                .group("cards")
                .pathsToMatch("/api/cards/**")
                .build();
    }
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EMSP Account and Card Management API")
                        .version("1.0.0")
                        .description("API for managing accounts and cards in EMSP system")
                        .contact(new Contact()
                                .name("EMSP Support")
                                .email("support@emsp.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}