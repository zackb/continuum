package continuum.core.atom;

import continuum.atom.Atom;
import continuum.atom.Particles;
import continuum.atom.AtomID;
import continuum.atom.ParticlesID;
import continuum.util.Bytes;

import java.nio.ByteBuffer;

/**
 * unique ID for series data in a Continuum
 * Created by zack on 2/11/16.
 */
public class SAtomID implements AtomID {

    private static final byte b = 0x0;

    private final transient byte[] cachedId;
    private final transient byte[] name;
    private final transient byte[] particles;
    private final transient byte[] timestamp;

    public SAtomID(byte[] bytes) {
        int timestampPos = 0;

        while (bytes[timestampPos] != b)
            timestampPos++;

        int particlesPos = timestampPos + 9;

        this.cachedId = bytes;
        name = Bytes.range(cachedId, 0, timestampPos);
        timestamp = Bytes.range(cachedId, timestampPos + 1, particlesPos);

        if (particlesPos < cachedId.length)
            particles = Bytes.range(cachedId, particlesPos + 1, cachedId.length);
        else particles = null;
    }

    public SAtomID(Atom atom) {
        ParticlesID pids = atom.particles() == null ? null : atom.particles().ID();
        name = Bytes.bytes(atom.name());
        particles = pids == null ? null : pids.bytes();
        int plen = particles == null ? 1 : particles.length;
        timestamp = Bytes.bytes(Long.MAX_VALUE - atom.timestamp());
        byte[] id = new byte[name.length + plen + timestamp.length + 2];

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(name);
        buff.put(b);
        buff.put(timestamp);
        buff.put(b);
        if (particles != null)
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
        return particles == null ? null : new AParticlesID(particles).particles();
    }

    @Override
    public long timestamp() {
        return Long.MAX_VALUE - Bytes.Long(timestamp);
    }
}
