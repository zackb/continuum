package com.dlvr.continuum.series.point;

import java.util.List;
import java.util.Map;

/**
 * The tags represented in a time series
 */
public interface Tags extends Map<String, String> {
    String put(String key, String value);
    String get(String key);
    List<String> names();
}