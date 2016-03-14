package com.dlvr.continuum.core.db;

import com.dlvr.continuum.db.ScanID;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.db.slice.Const;
import com.dlvr.continuum.util.Bytes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Series based ScanID implementation
 * Created by zack on 2/12/16.
 */
public class SScanID implements ScanID {

    private static final byte b = 0x0;

    private final byte[] id;

    private final String name;
    private final Particles particles;


    // TODO: disable scanning to exact name + particles match as depends on particle name ordering
    private static final boolean USE_PARTICLES = false;

    public SScanID(String name, Particles particles) {
        this.name = name;
        this.particles = particles;
        byte[] bname = Bytes.bytes(name);
        byte[] bparticles = null;
        if (USE_PARTICLES && particles != null)
            bparticles = encode(particles);
        else
            bparticles = new byte[0];
        id = new byte[bname.length + bparticles.length + 1];

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(bname);
        buff.put(b);
        buff.put(bparticles);
    }

    /**
     * build tag/values structure omitting values with wildcard
     * @param particles to use
     * @return bytes to use for scan
     */
    static byte[] encode(Particles particles) {
        byte[] bid = new byte[1024];
        List<String> names = particles == null ? new ArrayList<>() : particles.names();
        int len = names.size();

        int pos = 0;
        byte[] tmp;

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            tmp = Bytes.bytes(name);
            bid = Bytes.append(bid, pos, tmp);
            pos += tmp.length;
            bid[pos++] = b;
        }

        for (int i = 0; i < names.size(); i++) {
            String val = particles.get(names.get(i));
            if (val.equals(Const.SWILDCARD)) break;
            tmp = Bytes.bytes(val);
            bid = Bytes.append(bid, pos, tmp);
            pos += tmp.length;
            if (i < len - 1) bid[pos++] = b;
        }

        return Bytes.range(bid, 0, pos);
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
