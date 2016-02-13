package com.dlvr.continuum.core.db;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Tags;
import com.dlvr.continuum.db.AtomID;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;

/**
 * unique ID for key/value data in a Continuum
 * Created by zack on 2/12/16.
 */
public class KAtomID implements AtomID {

    private static final byte b = 0x0;

    private final transient byte[] cachedId;
    private final transient int[] positions;
    private final transient byte[] name;
    private final transient byte[] tags;
    private final transient byte[] timestamp;

    public KAtomID(byte[] bytes) {
        int count = 0;
        for (byte by : bytes)
            if (by == b) count++;
        positions = new int[count];
        positions[0] = 8;
        int posi = 1;
        for (int i = 9; i < bytes.length; i++) {
            if (bytes[i] == b) positions[posi++] = i;
        }
        this.cachedId = bytes;
        timestamp = Bytes.range(cachedId, 0, positions[0]);
        name = Bytes.range(cachedId, positions[0] + 1, positions[1]);
        tags = Bytes.range(cachedId, positions[1] + 1, cachedId.length);
    }

    public KAtomID(Atom atom) {
        name = Bytes.bytes(atom.name());
        tags = atom.tags().ID().bytes();
        timestamp = Bytes.bytes(atom.timestamp());

        byte[] id = new byte[timestamp.length + name.length + tags.length + 2];

        positions = new int[] { timestamp.length + 1, name.length + 1, tags.length + 2 };

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(timestamp);
        buff.put(b);
        buff.put(name);
        buff.put(b);
        buff.put(tags);
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
