package com.dlvr.continuum.db.impl;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.db.datum.Tags;
import com.dlvr.continuum.db.TagsID;
import com.dlvr.continuum.util.Bytes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tags id for series and time key value data
 * Created by zack on 2/11/16.
 */
public class NTagsID implements TagsID {

    private static final byte b = 0x0;

    private final transient byte[] cachedId;
    private final transient int[] positions;

    public NTagsID(byte[] bytes) {
        int count = 0;
        for (byte by : bytes)
            if (by == b) count++;

        positions = new int[count];

        int posi = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == b) positions[posi++] = i;
        }
        this.cachedId = bytes;
    }

    public NTagsID(Tags tags) {
        byte[] id = new byte[1024];
        List<String> names = tags.names();
        int len = names.size();

        int pos = 0;
        byte[] tmp;
        positions = new int[names.size() * 2];

        int posi = 0;
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            tmp = Bytes.bytes(name);
            id = Bytes.append(id, pos, tmp);
            pos += tmp.length;
            id[pos++] = b;
            positions[posi++] = i;
        }

        for (int i = 0; i < names.size(); i++) {
            String val = tags.get(names.get(i));
            tmp = Bytes.bytes(val);
            id = Bytes.append(id, pos, tmp);
            pos += tmp.length;
            positions[posi++] = i;
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

    //TODO: dog slow
    @Override
    public Tags tags() {
        int split = positions.length / 2;
        Map<String, String> ts = new HashMap<>();
        List<String> kv = new ArrayList<>();
        int prev = 0;
        for (int pos : positions) {
            kv.add(Bytes.String(Bytes.range(cachedId, prev, pos)));
            prev = pos + 1;
        }
        kv.add(Bytes.String(Bytes.range(cachedId, prev)));

        for (int i = 0; i < split + 1; i++) {
            String name = kv.get(i);
            String value = kv.get(i + split + 1);
            ts.put(name, value);
        }
        return Continuum.tags(ts);
    }
}
