package com.wechat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.util.JacksonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 */
@Slf4j
@Configuration
public class MapperConfig {

    @Bean
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = JacksonProvider.DEFAULT_OBJECT_MAPPER;
        return objectMapper;
    }

}
