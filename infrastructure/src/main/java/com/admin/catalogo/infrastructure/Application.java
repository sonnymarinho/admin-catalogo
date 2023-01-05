package com.admin.catalogo.infrastructure;


import com.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(WebServerConfig.class, args);
    }
}