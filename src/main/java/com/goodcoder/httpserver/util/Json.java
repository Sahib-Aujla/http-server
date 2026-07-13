package com.goodcoder.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class Json {
    private static ObjectMapper myObjectMapper;

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    public static JsonNode parse(String jsonSrc) throws JsonProcessingException {
        return getDefaultObjectMapper().readTree(jsonSrc);
    }

    public static <T> T fromJson(JsonNode node, Class<T> toClass) throws JsonProcessingException {
        return getDefaultObjectMapper().treeToValue(node, toClass);
    }

    public static JsonNode toJson(Object obj) {
        return getDefaultObjectMapper().valueToTree(obj);
    }

    public static String stringify(Object o) throws JsonProcessingException {
        return generateJson(o, false);
    }

    public static String stringifyPretty(Object o) throws JsonProcessingException {
        return generateJson(o, true);
    }

    private static String generateJson(Object o, boolean pretty) throws JsonProcessingException {
        ObjectWriter objwriter = getDefaultObjectMapper().writer();
        if (pretty) {
            objwriter = objwriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objwriter.writeValueAsString(o);
    }
}
