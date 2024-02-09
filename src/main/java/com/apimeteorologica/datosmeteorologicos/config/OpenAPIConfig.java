package com.jdeleonc.quickstart.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
/**
 *
 * @author Jan Carlo
 */
@Configuration
public class OpenAPIConfig {

  @Value("${bezkoder.openapi.dev-url}")
  private String devUrl;


  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Servidor local");

    Contact contact = new Contact();
    contact.setEmail("janleoncif11@gmail.com");
    contact.setName("Jan Carlo de León");
    

    

    Info info = new Info()
        .title("API Datos Meteorológicos")
        .version("1.0")
        .contact(contact)
        .description("API realizada en Java con el framework Spring Boot, la cual obtiene y presenta datos meteorológicos desde OpenWeatherMap.");

    return new OpenAPI().info(info).servers(List.of(devServer));
  }
}

