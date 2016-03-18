package continuum.core.slice;

import continuum.slice.ScanID;
import continuum.atom.Particles;
import continuum.util.Bytes;

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

    public KScanID(long end, String name, Particles particles) {
        this.name = name;
        this.particles = particles();

        byte[] bend = Bytes.bytes(end);   // end at time
        byte[] bname = Bytes.bytes(name);     // and name
        byte[] bparticles = SScanID.encode(particles); // same particles encoding logic as series
        id = new byte[bend.length + bname.length + bparticles.length + 2];

        ByteBuffer buff = ByteBuffer.wrap(id);
        buff.put(bend);
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
