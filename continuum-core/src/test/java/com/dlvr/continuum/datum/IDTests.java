package com.dlvr.continuum.datum;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.AtomID;
import com.dlvr.continuum.atom.ParticlesID;
import com.dlvr.continuum.core.atom.KAtomID;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.core.atom.SAtomID;
import com.dlvr.continuum.core.atom.NParticlesID;
import com.dlvr.continuum.util.Bytes;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.dlvr.continuum.Continuum.katom;
import static com.dlvr.continuum.Continuum.particles;
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
        ParticlesID id = new NParticlesID(e);
        Particles particles = id.particles();
        assertEquals("bat", particles.get("baz"));
        assertEquals("bar", particles.get("foo"));
        assertArrayEquals(e, id.bytes());
    }

    @Test
    public void testSeriesAtomIdMarshall() {
        long ts = 1455219259015L;
        byte[] e = {'z', 'a', 'm', 'e', 0x0, 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 'z', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r' };
        byte[] expected = Bytes.concat(e, (byte)0x0);
        expected = Bytes.concat(expected, Bytes.bytes(ts));
        Map<String, String> map = new HashMap<>();
        map.put("fooz", "bar");
        map.put("baz", "bat");
        Atom d = Continuum.satom().name("zame").particles(particles(map)).timestamp(ts).value(1235).build();
        assertArrayEquals(expected, d.ID().bytes());
    }

    @Test
    public void testSeriesAtomIdUnMarshall() {
        long ts = 1455219259015L;
        byte[] e = {'z', 'a', 'm', 'e', 0x0, 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 'z', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r' };
        byte[] expected = Bytes.concat(e, (byte)0x0);
        expected = Bytes.concat(expected, Bytes.bytes(ts));
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

        byte[] expected = Bytes.concat(Bytes.bytes(ts), (byte)0x0);
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
        byte[] expected = Bytes.concat(Bytes.bytes(ts), (byte)0x0);
        expected = Bytes.concat(expected, e);
        AtomID id = new KAtomID(expected);
        assertEquals("zame", id.name());
        assertEquals(ts, id.timestamp());
        assertEquals("bar", id.particles().get("fooz"));
        assertEquals("bat", id.particles().get("baz"));
    }
}