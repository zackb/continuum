package com.dlvr.continuum.series.point.impl;


import com.dlvr.continuum.series.point.Fields;

import java.util.HashMap;
import java.util.Map;

/**
 * Base Fields implementation
 */
public class BaseFields extends HashMap<String, Object> implements Fields {
    public BaseFields(Map<String, Object> raw) {
        super(raw);
    }

    @Override
    public String put(String key, Object value) {
        return (String)super.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return super.get(key);
    }
}
