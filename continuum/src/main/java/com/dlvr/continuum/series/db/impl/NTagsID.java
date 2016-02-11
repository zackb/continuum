package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.series.datum.Tags;
import com.dlvr.continuum.series.db.ID;
import com.dlvr.continuum.util.Bytes;

import java.util.List;

/**
 * Created by zack on 2/11/16.
 */
public class NTagsID implements ID {

    private transient String cachedId;

    public NTagsID(Tags tags) {
        String id = "";
        List<String> names = tags.names();
        int len = names.size();

        for (String name : names)
            id += name + "\\x00";

        for (int i = 0; i < names.size(); i++) {
            id += tags.get(names.get(i));
            if (i < len - 1) id += "\\x00";
        }
        cachedId = id;
    }

    @Override
    public String string() {
        return cachedId;
    }

    @Override
    public byte[] bytes() {
        return Bytes.bytes(string());
    }
}
