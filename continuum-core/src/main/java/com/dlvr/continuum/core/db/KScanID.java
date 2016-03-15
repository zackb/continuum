package com.dlvr.continuum.core.db;

import com.dlvr.continuum.db.ScanID;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;

/**
 * Time Key Values based ScanID implementation
 * Created by zack on 2/12/16.
 */
public class KScanID implements ScanID {

    private static final byte b = 0x0;

    private final byte[] id;

    private final String name;
    private final Particles particles;

    public KScanID(long start, String name, Particles particles) {
        this.name = name;
        this.particles = particles();

        byte[] bstart = Bytes.bytes(start);   // start at time
        byte[] bname = Bytes.bytes(name);     // and name
        byte[] bparticles = SScanID.encode(particles); // same particles encoding logic as series
        id = new byte[bstart.length + bname.length + bparticles.length + 2];

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(bstart);
        buff.put(b);
        buff.put(bname);
        buff.put(b);
        buff.put(bparticles);
    }

    @Override
    public String string() {
        return Bytes.String(id);
    }

    @Override
    public byte[] bytes() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Particles particles() {
        return particles;
    }
}
