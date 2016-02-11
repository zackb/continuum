package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.series.datum.Tags;
import com.dlvr.continuum.series.db.ID;
import com.dlvr.continuum.util.Bytes;

import java.util.List;

/**
 * Created by zack on 2/11/16.
 */
public class NTagsID implements ID {

    private static final byte b = 0x0;

    private transient byte[] cachedId;

    public NTagsID(Tags tags) {
        byte[] id = new byte[1024];
        List<String> names = tags.names();
        int len = names.size();

        int pos = 0;
        byte[] tmp;

        for (String name : names) {
            tmp = Bytes.bytes(name);
            id = Bytes.append(id, pos, tmp);
            pos += tmp.length;
            id[pos++] = b;
        }

        for (int i = 0; i < names.size(); i++) {
            String val = tags.get(names.get(i));
            tmp = Bytes.bytes(val);
            id = Bytes.append(id, pos, tmp);
            pos += tmp.length;
            if (i < len - 1) id[pos++] = b;
        }
        cachedId = Bytes.range(id, 0, pos);
    }

    @Override
    public String string() {
        return Bytes.String(cachedId);
    }

    @Override
    public byte[] bytes() {
        return cachedId;
    }
}
