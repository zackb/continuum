package com.dlvr.continuum.db.impl;

import com.dlvr.continuum.db.QueryID;
import com.dlvr.continuum.db.datum.Tags;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;

/**
 * Series based QueryID implementation
 * Created by zack on 2/12/16.
 */
public class NQueryID implements QueryID {

    private static final byte b = 0x0;

    private final byte[] id;

    public NQueryID(String name, Tags tags, double value, double start, double end) {
        byte[] bname = Bytes.bytes(name);
        byte[] btags = tags.ID().bytes();
        byte[] btimestamp = Bytes.bytes(start);
        id = new byte[bname.length + btags.length + btimestamp.length + 2];

        // TODO: build tag/value structure omitting values with wildcard

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(bname);
        buff.put(b);
        buff.put(btags);
        buff.put(b);
        buff.put(btimestamp);
    }

    @Override
    public String string() {
        return Bytes.String(id);
    }

    @Override
    public byte[] bytes() {
        return id;
    }
}
