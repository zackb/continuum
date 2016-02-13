package com.dlvr.continuum.db.datum;

import com.dlvr.continuum.db.TagsID;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The tags represented in a time db
 */
public interface Tags extends Map<String, String>, Serializable {
    String put(String key, String value);
    String get(String key);
    List<String> names();
    TagsID ID();
}