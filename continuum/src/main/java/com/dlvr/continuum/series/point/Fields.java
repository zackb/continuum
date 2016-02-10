package com.dlvr.continuum.series.point;

import java.util.Map;

/**
 * Fields within a time series data point
 */
public interface Fields extends Map<String, Object> {
    String put(String key, Object value);
    Object get(Object key);
}