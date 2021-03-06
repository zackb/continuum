package continuum.core.atom;

import continuum.Continuum;
import continuum.atom.Particles;
import continuum.atom.ParticlesID;
import continuum.util.Bytes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Particles ID for series and time key values data
 * Created by zack on 2/11/16.
 */
public class AParticlesID implements ParticlesID {

    private static final byte b = 0x0;

    private final transient byte[] cachedId;
    private final transient int[] positions;

    public AParticlesID(byte[] bytes) {
        int count = 0;
        for (byte by : bytes)
            if (by == b) count++;

        positions = new int[count];

        int posi = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == b) positions[posi++] = i;
        }
        this.cachedId = bytes;
    }

    public AParticlesID(Particles particles) {
        byte[] id = new byte[1024];
        List<String> names = particles.names();
        int len = names.size();

        int pos = 0;
        byte[] tmp;
        positions = new int[names.size() * 2];

        int posi = 0;
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            tmp = Bytes.bytes(name);
            id = Bytes.append(id, pos, tmp);
            pos += tmp.length;
            id[pos++] = b;
            positions[posi++] = i;
        }

        for (int i = 0; i < names.size(); i++) {
            String val = particles.get(names.get(i));
            if (val != null) {
                tmp = Bytes.bytes(val);
                id = Bytes.append(id, pos, tmp);
                pos += tmp.length;
            }
            positions[posi++] = i;
            if (i < len - 1) id[pos++] = b;
        }
        cachedId = Bytes.range(id, 0, pos);
    }

    @Override
    public String string() {
        return Bytes.String(cachedId);
    }

    @Override
    public byte[] bytes() {
        return cachedId;
    }

    //TODO: dog slow
    @Override
    public Particles particles() {
        int split = positions.length / 2;
        Map<String, String> ts = new HashMap<>();
        List<String> kv = new ArrayList<>();
        int prev = 0;
        for (int pos : positions) {
            kv.add(Bytes.String(Bytes.range(cachedId, prev, pos)));
            prev = pos + 1;
        }
        kv.add(Bytes.String(Bytes.range(cachedId, prev)));

        for (int i = 0; i < split + 1; i++) {
            String name = kv.get(i);
            String value = kv.get(i + split + 1);
            ts.put(name, value);
        }
        return Continuum.particles(ts);
    }
}
