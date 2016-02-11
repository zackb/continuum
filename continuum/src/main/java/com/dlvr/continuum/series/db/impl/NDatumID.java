package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.datum.Tags;
import com.dlvr.continuum.series.db.DatumID;
import com.dlvr.continuum.series.db.ID;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;

/**
 * Created by zack on 2/11/16.
 */
public class NDatumID implements DatumID {

    private static final byte b = 0x00;

    private transient byte[] cachedId;
    private final int[] positions;

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
    }

    public NDatumID(Datum datum) {
        byte[] name = Bytes.bytes(datum.name());
        byte[] tags = datum.tags().ID().bytes();
        byte[] ts = Bytes.bytes(datum.timestamp());
        byte[] id = new byte[name.length + tags.length + ts.length + 2];

        positions = new int[] {name.length + 1, tags.length + 1, ts.length + 2};

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(name);
        buff.put(b);
        buff.put(tags);
        buff.put(b);
        buff.put(ts);
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
        return Bytes.String(Bytes.range(cachedId, 0, positions[0]));
    }

    @Override
    public Tags tags() {
        return null;
    }

    @Override
    public long timestamp() {
        return 0;
    }
}
