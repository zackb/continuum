package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.db.ID;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;

/**
 * Created by zack on 2/11/16.
 */
public class NDatumID implements ID {

    private static final byte b = 0x00;

    private transient byte[] cachedId;

    public NDatumID(byte[] bytes) {
        this.cachedId = bytes;
    }

    public NDatumID(Datum datum) {
        byte[] name = Bytes.bytes(datum.name());
        byte[] tags = datum.tags().ID().bytes();
        byte[] ts = Bytes.bytes(datum.timestamp());
        byte[] id = new byte[name.length + tags.length + ts.length + 2];
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
}
