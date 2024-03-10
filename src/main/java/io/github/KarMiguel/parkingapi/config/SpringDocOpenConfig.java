package io.github.KarMiguel.parkingapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("REST API - Spring Park")
                                .description("Api para estacionamento de veículos")
                                .version("v1")
                                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                                .contact(new Contact().name("Carlos Miguel").email("carlosmiguel.dsa12@gmail.com"))
                );
    }
}
