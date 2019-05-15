package com.tdwl.wife.sql.utils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static com.fasterxml.jackson.databind.ObjectMapper objectFasterMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    public static <T> T parseToObject(InputStream is, Class<T> toClass) {
        try {
            return (T) objectMapper.readValue(is, toClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseToObject(byte[] b, int offset, int len, Class<T> valueType) {
        try {
            return (T) objectMapper.readValue(b, offset, len, valueType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseFasterToObject(String json, Class<T> toClass) {
        try {
            return (T) objectFasterMapper.readValue(json, toClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseToObject(String json, Class<T> toClass) {
        try {
            return (T) objectMapper.readValue(json, toClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Map parseToMap(String json) {
        return parseToObject(json, Map.class);
    }

    public static Map parseToMap(byte[] b) {
        if (b == null || b.length == 0) {
            return null;
        }
        return parseToObject(b, 0, b.length, Map.class);
    }

    public static Map parseToMap(InputStream is) {
        return parseToObject(is, Map.class);
    }

    public static String parseToJson(Object o) {
        if (o == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
