package com.carRestApi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Car REST API", version = "1.0", description = "Endpoints below",
        contact = @Contact(url = "https://github.com/przemyslaw-orpel", name = "Contact: przemyslaw-orpel")))
public class CarRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRestApiApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
