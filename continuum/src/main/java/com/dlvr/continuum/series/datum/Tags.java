package com.dlvr.continuum.series.datum;

import com.dlvr.continuum.series.db.ID;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The tags represented in a time series
 */
public interface Tags extends Map<String, String>, Serializable {
    String put(String key, String value);
    String get(String key);
    List<String> names();
    ID ID();
}