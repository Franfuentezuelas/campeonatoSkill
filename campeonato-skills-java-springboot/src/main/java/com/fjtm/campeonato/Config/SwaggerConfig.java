package com.fjtm.campeonato.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class SwaggerConfig {

    // con este bean puedes configurar la ruta 
    // @Bean
    // public GroupedOpenApi publicApi() {
    //     return GroupedOpenApi.builder()
    //             .group("public")
    //             .pathsToMatch("/api/**")  // Asegúrate de que esté configurado según tus rutas
    //             .build();
    // }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("API Campeonato Skills")
                    .version("1.0.0")
                    .description("Documentacion para la API Campeonato Skills.")
                    .termsOfService("https://www.ejemplo.com/terminos")
                    .contact(new Contact()
                        .name("Soporte de API")
                        .url("https://www.fran.com/contacto")
                        .email("soporte@fran.com"))
                    .license(new License()
                        .name("Licencia Fran")
                        .url("https://www.fran.com/licencia")));
    }
}

