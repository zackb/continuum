package com.dlvr.continuum.db.impl;

import com.dlvr.continuum.db.datum.Datum;
import com.dlvr.continuum.db.datum.Tags;
import com.dlvr.continuum.db.DatumID;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;

/**
 * unique ID for series data in a Zontinuum
 * Created by zack on 2/11/16.
 */
public class NDatumID implements DatumID {

    private static final byte b = 0x0;

    private final transient byte[] cachedId;
    private final transient int[] positions;
    private final transient byte[] name;
    private final transient byte[] tags;
    private final transient byte[] timestamp;

    public NDatumID(byte[] bytes) {
        int count = 0;
        for (byte by : bytes)
            if (by == b) count++;
        positions = new int[count];
        int posi = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == b) positions[posi++] = i;
        }
        this.cachedId = bytes;
        name = Bytes.range(cachedId, 0, positions[0]);
        tags = Bytes.range(cachedId, positions[0] + 1, positions[positions.length - 1] - 2);
        timestamp = Bytes.range(cachedId, positions[positions.length - 1] - 1, cachedId.length);
    }

    public NDatumID(Datum datum) {
        name = Bytes.bytes(datum.name());
        tags = datum.tags().ID().bytes();
        timestamp = Bytes.bytes(datum.timestamp());
        byte[] id = new byte[name.length + tags.length + timestamp.length + 2];

        positions = new int[] { name.length + 1, tags.length + 1, timestamp.length + 2 };

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(name);
        buff.put(b);
        buff.put(tags);
        buff.put(b);
        buff.put(timestamp);
        cachedId = id;
    }

    @Override
    public String string() {
        return Bytes.String(cachedId);
    }

    @Override
    public byte[] bytes() {
        return cachedId;
    }

    @Override
    public String name() {
        return Bytes.String(name);
    }

    @Override
    public Tags tags() {
        return new NTagsID(tags).tags();
    }

    @Override
    public long timestamp() {
        return Bytes.Long(timestamp);
    }
}
