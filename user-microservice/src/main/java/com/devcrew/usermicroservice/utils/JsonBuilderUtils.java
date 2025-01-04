package com.devcrew.usermicroservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.transaction.Transactional;

import java.time.format.DateTimeFormatter;

@Transactional
public class JsonBuilderUtils {
    public static String jsonBuilder(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        objectMapper.registerModule(module);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            String jsonString = objectMapper.writeValueAsString(object);
            return jsonString.replace("\"", "\\\"");
        } catch (Exception e) {
            throw new Exception("Error while converting object to json: " + e.getMessage(), e);
        }
    }
}
