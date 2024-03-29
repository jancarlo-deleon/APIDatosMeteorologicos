package com.apimeteorologica.datosmeteorologicos.config;

import io.swagger.v3.oas.models.Components;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 *
 * @author Jan Carlo
 */
@Configuration
public class OpenAPIConfig {

    @Value("${datosmeteorologicos.openapi.dev-url}")
    private String devUrl;
    @Value("${datosmeteorologicos.openapi.docker-url}")
    private String dockerUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Servidor local");

        Server prodServer = new Server();
        prodServer.setUrl(dockerUrl);
        prodServer.setDescription("Servidor para pruebas con Docker");

        Contact contact = new Contact();
        contact.setEmail("janleoncif11@gmail.com");
        contact.setName("Jan Carlo de León");

        Info info = new Info()
                .title("API Datos Meteorológicos")
                .version("1.0")
                .contact(contact)
                .description("API realizada en Java con el framework Spring Boot, la cual obtiene y presenta datos meteorológicos desde OpenWeatherMap.");

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer)).components(new Components()
                .addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")));
    }
}
