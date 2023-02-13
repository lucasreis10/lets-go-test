package com.catalogo.infrastructure.configuration;

import com.catalogo.infrastructure.configuration.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    public ObjectMapper objectMapper() {
        return Json.mapper();
    }

}
