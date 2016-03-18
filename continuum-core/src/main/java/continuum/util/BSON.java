package continuum.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;

import java.util.Map;

/**
 * BSON http://bsonspec.org/
 * Created by zack on 2/23/16.
 */
public class BSON {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper(new SmileFactory());
        JSON.configureMapper(mapper);
    }

    public static byte[] encode(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            System.err.println("Failed encoding object: " + obj);
        }
        return null;
    }

    public static <T> T decodeObject(byte[] bytes, Class<T> clazz) {
        try {
            return mapper.readValue(bytes, clazz);
        } catch (Exception e) {
            System.err.println("Failed decoding object: " + Bytes.String(bytes));
            e.printStackTrace();
        }
        return null;
    }

    public static Map decode(byte[] bytes) {
        try {
            return (Map)decodeObject(bytes, JSON.MAP_CLASS);
        } catch (Exception e) {
            System.err.println("Failed decoding object: " + Bytes.String(bytes));
        }
        return null;
    }
}
