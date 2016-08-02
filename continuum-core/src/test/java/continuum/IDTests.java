package continuum;

import continuum.atom.AtomID;
import continuum.atom.ParticlesID;
import continuum.core.atom.KAtomID;
import continuum.atom.Atom;
import continuum.atom.Particles;
import continuum.core.atom.SAtomID;
import continuum.core.atom.AParticlesID;
import continuum.util.Bytes;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static continuum.Continuum.katom;
import static continuum.Continuum.particles;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by zack on 2/10/16.
 */
public class IDTests {
    @Test
    public void testTagsIdMmarshall() {
        byte[] e = { 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r' };
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "bat");
        Particles particles = particles(map);
        assertArrayEquals(e, particles.ID().bytes());
    }

    @Test
    public void testTagsIdUnmarshallMeta() {
        byte[] e = { 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r' };
        ParticlesID id = new AParticlesID(e);
        Particles particles = id.particles();
        assertEquals("bat", particles.get("baz"));
        assertEquals("bar", particles.get("foo"));
        assertArrayEquals(e, id.bytes());
    }

    @Test
    public void testSeriesAtomIdMarshall() {
        long ts = 1455219259015L;
        byte[] expected = {'z', 'a', 'm', 'e', 0x0, 0x7F, (byte)0xFF, (byte)0xFE, (byte)0xAD, (byte)0x2E, (byte)0x2C, (byte)0x49, (byte)0x78, (byte)0x0, 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 'z', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r' };
        Map<String, String> map = new HashMap<>();
        map.put("fooz", "bar");
        map.put("baz", "bat");
        Atom d = Continuum.satom().name("zame").particles(particles(map)).timestamp(ts).value(1235).build();
        assertArrayEquals(expected, d.ID().bytes());
    }

    @Test
    public void testSeriesAtomIdUnMarshall() {
        long ts = 1455219259015L;
        byte[] expected = {'z', 'a', 'm', 'e', 0x0, (byte)0x7F, (byte)0xFF, (byte)0xFE, (byte)0xAD, (byte)0x2E, (byte)0x2C, (byte)0x49, (byte)0x78, 0x0, 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 'z', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r'};
        AtomID id = new SAtomID(expected);
        assertEquals("zame", id.name());
        assertEquals(ts, id.timestamp());
        assertEquals("bar", id.particles().get("fooz"));
        assertEquals("bat", id.particles().get("baz"));
    }

    @Test
    public void testKeyValueAtomIdMarshall() {
        long ts = 1455219259015L;
        byte[] e = {'z', 'a', 'm', 'e', 0x0, 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 'z', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r' };

        byte[] expected = Bytes.concat(Bytes.bytes(Long.MAX_VALUE - ts), (byte)0x0);
        expected = Bytes.concat(expected, e);
        Map<String, String> map = new HashMap<>();
        map.put("fooz", "bar");
        map.put("baz", "bat");
        Atom d = katom().name("zame").particles(particles(map)).timestamp(ts).value(1235).build();
        assertArrayEquals(expected, d.ID().bytes());
    }

    @Test
    public void testKeyValueAtomIdUnMarshall() {
        long ts = 1455219259015L;
        byte[] e = {'z', 'a', 'm', 'e', 0x0, 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 'z', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r' };
        byte[] expected = Bytes.concat(Bytes.bytes(Long.MAX_VALUE - ts), (byte)0x0);
        expected = Bytes.concat(expected, e);
        AtomID id = new KAtomID(expected);
        assertEquals("zame", id.name());
        assertEquals(ts, id.timestamp());
        assertEquals("bar", id.particles().get("fooz"));
        assertEquals("bat", id.particles().get("baz"));
    }
}
