package com.dlvr.continuum.core.atom;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.atom.AtomID;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;

/**
 * unique ID for key/values data in a Continuum
 * Created by zack on 2/12/16.
 */
public class KAtomID implements AtomID {

    private static final byte b = 0x0;

    private final transient byte[] cachedId;
    private final transient int[] positions;
    private final transient byte[] name;
    private final transient byte[] particles;
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
        particles = Bytes.range(cachedId, positions[1] + 1, cachedId.length);
    }

    public KAtomID(Atom atom) {
        name = Bytes.bytes(atom.name());
        particles = atom.particles().ID().bytes();
        timestamp = Bytes.bytes(atom.timestamp());

        byte[] id = new byte[timestamp.length + name.length + particles.length + 2];

        positions = new int[] { timestamp.length + 1, name.length + 1, particles.length + 2 };

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(timestamp);
        buff.put(b);
        buff.put(name);
        buff.put(b);
        buff.put(particles);
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
    public Particles particles() {
        return new NParticlesID(particles).particles();
    }

    @Override
    public long timestamp() {
        return Bytes.Long(timestamp);
    }
}
