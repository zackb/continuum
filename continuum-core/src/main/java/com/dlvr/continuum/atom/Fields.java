package com.dlvr.continuum.atom;

import java.io.Serializable;
import java.util.Map;

/**
 * Fields within a time translator data atom
 */
public interface Fields extends Map<String, Object>, Serializable {
    String put(String key, Object value);
    Object get(Object key);
}