/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author aar1069
 */
@Configuration
public class DateFormatterConfiguration {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    
    @Bean
    public ObjectMapper objectMapper() {
        JsonMapper mapper = JsonMapper.builder()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_OPTIONALS)
                .build();
        mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
