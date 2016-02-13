package com.dlvr.continuum.datum;

import java.io.Serializable;
import java.util.Map;

/**
 * Fields within a time db data datum
 */
public interface Fields extends Map<String, Object>, Serializable {
    String put(String key, Object value);
    Object get(Object key);
}