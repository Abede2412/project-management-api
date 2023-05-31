package com.group2.projectmanagementapi.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
class SwaggerConfiguration {
    @Bean
    OpenAPI simpleAppOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Simple App API")
                        .description("Spring simple application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
