package com.dlvr.continuum.db.datum.impl;

import com.dlvr.continuum.db.TagsID;
import com.dlvr.continuum.db.datum.Tags;
import com.dlvr.continuum.db.impl.NTagsID;

import java.util.*;

/**
 * Base Tags implementation
 */
public class NTags extends HashMap<String, String> implements Tags {

    private transient List<String> sortedNames;

    NTags() { super(); }

    public NTags(Map<String, String> raw) {
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
    public TagsID ID() {
        // TODO: Cache? Mutability?
        return new NTagsID(this);
    }
}
