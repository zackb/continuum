package com.dlvr.continuum.series.datum.impl;

import com.dlvr.continuum.series.datum.Tags;

import java.util.*;

/**
 * Base Tags implementation
 */
public class BaseTags extends HashMap<String, String> implements Tags {

    private transient List<String> sortedNames;
    private transient String cachedId;

    BaseTags() { super(); }

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

    @Override
    public String ID() {
        if (cachedId == null) {
            String id = "";
            List<String> names = names();
            int len = names.size();

            for (String name : names)
                id += name + "\\x00";

            for (int i = 0; i < names.size(); i++) {
                id += get(names.get(i));
                if (i < len - 1) id += "\\x00";
            }
            cachedId = id;
        }
        return cachedId;
    }
}
