package com.padaks.todaktodak.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String AUTH_TOKEN = "Authorization";

    SecurityScheme apiAuth = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .in(SecurityScheme.In.HEADER)
            .scheme("bearer")
            .bearerFormat("JWT")
            .name(AUTH_TOKEN);

    SecurityRequirement securityItem = new SecurityRequirement()
            .addList(AUTH_TOKEN);

    @Bean
    public OpenAPI openAPI(){
        var info = new Info();
        info.title("토닥파닥 API - community");
        info.description("토닥파닥의 community API 다!!");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(AUTH_TOKEN, apiAuth))
                .addSecurityItem(securityItem)
                .info(info);
    }
}
