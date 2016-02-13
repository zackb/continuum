package com.dlvr.continuum.core.db;

import com.dlvr.continuum.db.QueryID;
import com.dlvr.continuum.datum.Tags;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;

/**
 * Time Key Value based QueryID implementation
 * Created by zack on 2/12/16.
 */
public class KVQueryID implements QueryID {

    private static final byte b = 0x0;

    private final byte[] id;

    public KVQueryID(double start, String name, Tags tags) {

        byte[] bstart = Bytes.bytes(start);   // start at time
        byte[] bname = Bytes.bytes(name);     // and name
        byte[] btags = SQueryID.encode(tags); // same tags encoding logic as series
        id = new byte[bstart.length + bname.length + btags.length + 2];

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(bstart);
        buff.put(b);
        buff.put(bname);
        buff.put(b);
        buff.put(btags);

        throw new Error("This needs testing");
    }

    @Override
    public String string() {
        return null;
    }

    @Override
    public byte[] bytes() {
        return new byte[0];
    }
}
