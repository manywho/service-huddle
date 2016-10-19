package com.manywho.services.huddle.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.manywho.sdk.services.factories.ObjectMapperFactory;
import com.mashape.unirest.http.ObjectMapper;

import java.io.IOException;

public class HuddleObjectMapper implements ObjectMapper {
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    @Override
    public <T> T readValue(String value, Class<T> valueType) {
        try {
            return objectMapper.readValue(value, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String writeValue(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
