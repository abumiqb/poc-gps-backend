package no.itnow.location.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gpsBackendOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("GPS Backend API")
                        .version("v1")
                        .description("API for active trip sessions, live positions and trip history.")
                        .contact(new Contact().name("GPS Backend")));
    }
}
