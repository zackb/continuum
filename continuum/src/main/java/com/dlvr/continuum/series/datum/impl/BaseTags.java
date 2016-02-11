package com.dlvr.continuum.series.datum.impl;

import com.dlvr.continuum.series.datum.Tags;
import com.dlvr.continuum.series.db.ID;
import com.dlvr.continuum.series.db.impl.NTagsID;

import java.util.*;

/**
 * Base Tags implementation
 */
public class BaseTags extends HashMap<String, String> implements Tags {

    private transient List<String> sortedNames;
    private transient NTagsID cachedId;

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
    public ID ID() {
        if (cachedId == null) {
            cachedId = new NTagsID(this);
        }
        return cachedId;
    }
}
