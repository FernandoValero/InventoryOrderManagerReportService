package com.manager.order.inventory.reportservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de " + applicationName)
                        .description("Documentación del servicio " + applicationName)
                        .version("1.0")
                        .contact(new Contact()
                                .name("Tu Empresa")
                                .email("contacto@tuempresa.com")
                                .url("https://www.tuempresa.com")))
                .servers(Arrays.asList(
                        new Server().url("/").description("Report Service")
                ));
    }
}