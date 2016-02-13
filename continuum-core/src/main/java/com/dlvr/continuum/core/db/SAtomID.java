package com.dlvr.continuum.core.db;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.db.AtomID;
import com.dlvr.continuum.db.ParticlesID;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;

/**
 * unique ID for series data in a Continuum
 * Created by zack on 2/11/16.
 */
public class SAtomID implements AtomID {

    private static final byte b = 0x0;

    private final transient byte[] cachedId;
    private final transient int[] positions;
    private final transient byte[] name;
    private final transient byte[] particles;
    private final transient byte[] timestamp;

    public SAtomID(byte[] bytes) {
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
        particles = Bytes.range(cachedId, positions[0] + 1, positions[positions.length - 1] - 2);
        timestamp = Bytes.range(cachedId, positions[positions.length - 1] - 1, cachedId.length);
    }

    public SAtomID(Atom atom) {
        ParticlesID pids = atom.particles() == null ? null : atom.particles().ID();
        name = Bytes.bytes(atom.name());
        particles = pids == null ? null : pids.bytes();
        int plen = particles == null ? 1 : particles.length;
        timestamp = Bytes.bytes(atom.timestamp());
        byte[] id = new byte[name.length + plen + timestamp.length + 2];

        positions = new int[] { name.length + 1, plen + 1, timestamp.length + 2 };

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(name);
        buff.put(b);
        if (particles != null)
            buff.put(particles);
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
    public Particles particles() {
        return new NParticlesID(particles).particles();
    }

    @Override
    public long timestamp() {
        return Bytes.Long(timestamp);
    }
}