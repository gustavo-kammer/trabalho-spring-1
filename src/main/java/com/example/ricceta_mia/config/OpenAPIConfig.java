package com.example.ricceta_mia.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ricceta Mia API")
                        .description("API de compartilhamento de receitas, perfis e interações. "
                                + "Cadastre-se em /auth/register e faça login em /auth/login. "
                                + "No botão Authorize, informe somente o token retornado, sem o prefixo Bearer. "
                                + "Login e cadastro são públicos; as demais operações exigem JWT. "
                                + "A configuração atual retorna 403 para acesso protegido sem token válido.")
                        .version("v1.0"))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
