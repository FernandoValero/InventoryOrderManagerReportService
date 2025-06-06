package com.manager.order.inventory.reportservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
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
                        .description("Documentación del servicio " + applicationName + ". Servicios encargado de generar reportes de ventas en formato PDF y Excel")
                        .version("1.0"))
                .servers(Arrays.asList(
                        new Server().url("/api/v1/").description("Report Service")
                ))
                .tags(Arrays.asList(
                        new Tag().name("Reportes").description("Generación de reportes analíticos de ventas. Permite obtener documentos en diferentes formatos (PDF, Excel)\n" +
                                "    con información detallada de ventas filtradas por:\n" +
                                "    - Usuario\n" +
                                "    - Cliente \n" +
                                "    - Rango de fechas\n" +
                                "    - Mes específico\n" +
                                "    - Año específico\n" +
                                "    - Producto\n")
                ));
    }
}