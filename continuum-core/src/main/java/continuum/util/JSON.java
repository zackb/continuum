package continuum.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON Curios
 * Created by zack on 2/23/16.
 */
public class JSON {

    private static final ObjectMapper mapper;
    public static final Class MAP_CLASS;

    static {
        mapper = new ObjectMapper();
        configureMapper(mapper);

        MAP_CLASS = new HashMap<String, Object>().getClass();
    }

    public static void configureMapper(ObjectMapper objectMapper) {

        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
                //.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //.enableDefaultTyping()
    }

    public static <T> String encode(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            System.err.println("Failed encoding object: " + obj);
        }
        return null;
    }

    /**
     * JSON string pretty formatted
     * @param obj to format as string
     * @return prettified json string
     */
    public static <T> String pretty(T obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            System.err.println("Failed encoding object: " + obj);
        }
        return null;
    }

    public static <T> T decodeObject(String data, Class<T> clazz) {
        try {
            return mapper.readValue(data.getBytes("UTF-8"), clazz);
        } catch (Exception e) {
            System.err.println("Failed decoding object: " + clazz + " : " + data);
        }
        return null;
    }

    public static <T> List<T> decodeList(String data, Class<T> clazz) {
        try {
            return mapper.readValue(data, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            System.err.println("Failed decoding list: " + clazz + " : " + data);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> decode(String data) {
        return (Map<String, Object>)decodeObject(data, MAP_CLASS);
    }
}
