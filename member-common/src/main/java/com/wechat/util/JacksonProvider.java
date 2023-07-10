package com.wechat.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 */
public class JacksonProvider implements JsonProvider {
    private static final Logger log = LoggerFactory.getLogger(JacksonProvider.class);

    public static final ObjectMapper DEFAULT_OBJECT_MAPPER = initObjectMapper();

    public JacksonProvider() {
    }

    private static ObjectMapper initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        objectMapper.registerModule(module);
        objectMapper.registerModule(new GuavaModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    @Override
    public Map parse(String text) {
        try {
            return (Map)DEFAULT_OBJECT_MAPPER.readValue(text, Map.class);
        } catch (IOException var3) {
            log.error("Failed to parse text " + text + " to map with error ", var3);
            throw new IllegalArgumentException("Error parsing '" + text + "' to map with error", var3);
        }
    }

    @Override
    public <K, V> Map<K, V> parse(String text, Class<K> keyType, Class<V> valueType) {
        return this.parse(text, Map.class, keyType, valueType);
    }

    @Override
    public <T extends Map, K, V> Map<K, V> parse(String text, Class<T> mapType, Class<K> keyType, Class<V> valueType) {
        try {
            MapType javaType = DEFAULT_OBJECT_MAPPER.getTypeFactory().constructMapType(mapType, keyType, valueType);
            return (Map)DEFAULT_OBJECT_MAPPER.readValue(text, javaType);
        } catch (IOException var6) {
            log.error("Failed to parse text " + text + " to map with error ", var6);
            throw new IllegalArgumentException("Error parsing '" + text + "' to map with error", var6);
        }
    }

    @Override
    public <T> T parse(String text, Class<T> targetType) {
        try {
            return DEFAULT_OBJECT_MAPPER.readValue(text, targetType);
        } catch (IOException var4) {
            log.error("Failed to parse text " + text + " with error ", var4);
            throw new IllegalArgumentException("Error parsing '" + text + "' with error", var4);
        }
    }

    @Override
    public <T> List<T> parseList(String text, Class<T> elementType) {
        try {
            JavaType listType = DEFAULT_OBJECT_MAPPER.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, new Class[]{elementType});
            return (List)DEFAULT_OBJECT_MAPPER.readValue(text, listType);
        } catch (IOException var4) {
            log.error("Failed to parse text " + text + " to list with error ", var4);
            throw new IllegalArgumentException("Error parsing '" + text + "' to list with error", var4);
        }
    }

    @Override
    public String convertObj(Object obj) {
        try {
            return DEFAULT_OBJECT_MAPPER.writeValueAsString(obj);
        } catch (IOException var3) {
            log.error("Failed to convert obj " + obj + " to Json text with error ", var3);
            throw new IllegalArgumentException("Error converting object '" + obj + "' to Json text with err ", var3);
        }
    }

    @Override
    public <T> T convertObj(Object obj, Class<T> targetType) {
        return DEFAULT_OBJECT_MAPPER.convertValue(obj, targetType);
    }
}
