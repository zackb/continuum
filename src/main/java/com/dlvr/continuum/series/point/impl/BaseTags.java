package com.dlvr.continuum.series.point.impl;

import com.dlvr.continuum.series.point.Tags;

import java.util.*;

/**
 * Base Tags implementation
 */
public class BaseTags extends HashMap<String, String> implements Tags {

    private List<String> sortedNames;

    public BaseTags(Map<String, String> raw) {
        super(raw);
    }

    @Override
    public String get(String key) {
        return super.get(key);
    }

    @Override
    public List<String> names() {
        if (sortedNames == null) {
            List<String> sn = new ArrayList<>(keySet());
            Collections.sort(sn);
            sortedNames = sn;
        }
        return sortedNames;
    }

    @Override
    public String put(String key, String value) {
        return super.put(key, value);
    }
}
