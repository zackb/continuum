package com.dlvr.continuum;

import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.Scan;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.util.datetime.Interval;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Continuum core api test
 * Created by zack on 2/10/16.
 */
public class ContinuumTest {

    FileSystemReference reference = new FileSystemReference("/tmp/continuum/test.com.dlvr.continuum.ContinuumTest");

    @Test
    public void testHello() {
        Continuum.sayHello();
    }

    @Test
    public void testSanity() throws Exception {
        Continuum continuum = null;
        try {
            continuum = Continuum.continuum()
                    .name("testSanity")
                    .dimension(Continuum.Dimension.TIME)
                    .base(reference)
                    .open();
        } finally {
            if (continuum != null) continuum.delete();
        }
    }

    @Test
    public void testWriteRead() throws Exception {
        Continuum continuum = null;
        try {
            continuum = Continuum.continuum()
                    .dimension(Continuum.Dimension.SPACE)
                    .name("testWriteRead")
                    .base(reference)
                    .open();

            long time = System.currentTimeMillis();
            Atom atom = continuum
                            .atom()
                            .name("test1")
                            .value(1.2)
                            .build();

            // auto assigned timestamp if not supplied
            long diff = Math.abs(atom.timestamp() - time);

            assertTrue(diff < 5);

            continuum.write(atom);

            atom = continuum.get(atom.ID());
            assertEquals("test1", atom.name());
            assertEquals(1.2, atom.values().value(), 0.0);
            assertEquals(1.2, atom.values().max(), 0.0);
            assertEquals(1.2, atom.values().min(), 0.0);
            assertEquals(1, atom.values().count(), 0);
            assertEquals(1.2, atom.values().sum(), 0.0);
        } finally {
            if (continuum != null) continuum.delete();
        }
    }

    @Test
    public void testWriteReadTime() throws Exception {
        Continuum continuum = null;
        try {
            continuum = Continuum.continuum()
                    .dimension(Continuum.Dimension.TIME)
                    .name("testWriteReadTime")
                    .base(reference)
                    .open();

            Atom atom = continuum
                    .atom()
                    .name("user123")
                    .particles("device", "iphone")
                    .value(1)
                    .build();

            continuum.write(atom);

            atom = continuum
                    .atom()
                    .name("user123")
                    .particles("asn", "12334")
                    .value(1)
                    .build();

            continuum.write(atom);

            atom = continuum.get(atom.ID());

            assertEquals("user123", atom.name());
            assertEquals(1, atom.values().value(), 0.0);
            assertEquals(1, atom.values().max(), 0.0);
            assertEquals(1, atom.values().min(), 0.0);
            assertEquals(1, atom.values().count(), 0);
            assertEquals(1, atom.values().sum(), 0.0);


            Slice slice = continuum.slice(
                            continuum.scan("user123")
                                    .start(System.currentTimeMillis() + 1)
                                .build());

            assertEquals(2, slice.atoms().size());
            assertEquals(1, slice.values().value(), 0);

        } finally {
            if (continuum != null) continuum.delete();
        }
    }

    @Test
    public void testSliceIntervals() throws Exception {
        Continuum continuum = null;
        try {
            continuum = Continuum.continuum()
                    .name("testSliceIntervals")
                    .dimension(Continuum.Dimension.SPACE)
                    .base(reference)
                    .open();
            Map<String, String> particles = new HashMap<>();
            particles.put("Foo", "bar");

            Atom atom = continuum
                    .atom("a1")
                    .particles(Continuum.particles(particles))
                    .timestamp(1455088007777L) //Wed Feb 10 07:06:38 UTC 2016
                    .value(10)
                    .build();
            continuum.write(atom);

            atom = continuum
                    .atom("a1")
                    .particles(Continuum.particles(particles))
                    .timestamp(1455174528388L) // Thu Feb 11 07:09:06 UTC 2016
                    .value(5)
                    .build();
            continuum.write(atom);

            atom = continuum
                    .atom("a1")
                    .particles(Continuum.particles(particles))
                    .timestamp(1455174529388L) // Thu Feb 11 07:09:07 UTC 2016
                    .value(100)
                    .build();
            continuum.write(atom);

            atom = continuum
                    .atom("a1")
                    .particles(Continuum.particles(particles))
                    .timestamp(1455260994255L) // Fri Feb 12 07:09:41 UTC 2016
                    .value(-0.000005)
                    .build();
            continuum.write(atom);

            Scan scan = continuum
                    .scan("a1")
                    .particles(Continuum.particles(particles))
                    .start(System.currentTimeMillis())
                    .end(1455088000007L)
                    .function(Function.AVG)
                    .build();

            Double val = continuum.slice(scan).values().value();
            assertEquals(28.74999875, val, 0.001);

            scan = continuum
                    .scan("a1")
                    .particles(Continuum.particles(particles))
                    .start(System.currentTimeMillis())
                    .end(1455088000007L)
                    .interval(Interval.valueOf("1h"))
                    .function(Function.AVG)
                    .build();

            Slice res = continuum.slice(scan);
            assertEquals(3, res.slices().size());
            assertEquals(10.0D, res.slice(2).values().value(), 0.000001);
            assertEquals(-0.000005D, res.slice(0).values().value(), 0.000001);
            assertEquals(52.5D, res.slice(1).values().value(), 0.000001);

        } finally {
            if (continuum != null) continuum.delete();
        }
    }

    @Test
    public void testSpaceMany() throws Exception {
        Continuum continuum = null;
        try {
            long now = 1456352961000L;
            //now = now - Math.abs(now % 1000);
            continuum = Continuum.continuum()
                    .name("testSpaceMany")
                    .dimension(Continuum.Dimension.SPACE)
                    .base(reference)
                    .open();

            Atom atom = continuum.atom().name("test1")
                    .value(10.0)
                    .timestamp(now - 90000)
                    .build();
            continuum.write(atom);

            atom = continuum.atom().name("test0")
                    .value(0.4545)
                    .timestamp(now - 80000)
                    .build();
            continuum.write(atom);

            atom = continuum.atom().name("test5")
                    .value(5555.55556)
                    .timestamp(now - 70000)
                    .build();
            continuum.write(atom);


            Map<String, String> tags = new HashMap<>();
            tags.put("provider", "limelight");
            tags.put("network", "12345");
            Particles particles = Continuum.particles(tags);

            atom = continuum.atom().name("test5")
                    .particles(particles)
                    .value(5555.001)
                    .timestamp(now - 68000)
                    .build();
            continuum.write(atom);

            long end = now - 100000;

            // test scanning all atoms
            Slice result = continuum.slice(
               continuum.scan("test0").end(end).build());

            assertNotNull(result);
            assertNotNull(result.atoms());
            assertEquals(1, result.atoms().size());


            // test averaging
            result = continuum.slice(
               continuum.scan("test5").end(end).function(Function.AVG).build());

            assertEquals(5555.27828, result.values().value(), 0.000d);

            // test count interval
            result = continuum.slice(
                continuum.scan("test5").end(end).function(Function.COUNT).interval(Interval.valueOf("1s")).build());

            assertNotNull(result);
            assertEquals("interval", result.name());
            assertEquals(2, result.slices().size());

            assertEquals(1.0, result.slices().get(0).values().value(), 0);
            assertEquals(1.0, result.slices().get(1).values().value(), 0);

            // test scan all atoms
            result = continuum.slice(
                    continuum.scan("test5").end(end).build());

            assertNull(result.slices());
            assertEquals(2, result.atoms().size());

            long ts = result.atoms().get(0).timestamp();
            for (Atom a : result.atoms()) {
                assertTrue(a.timestamp() <= ts);
                ts = a.timestamp();
            }

            Atom a = result.atoms().get(0);
            assertEquals("test5", a.name());
            assertEquals(2, a.particles().size());
            assertEquals("limelight", a.particles().get("provider"));
            assertEquals("12345", a.particles().get("network"));
            assertEquals(5555.001, a.values().value(), 0);
            assertEquals(now - 68000, a.timestamp(), 0);

            a = result.atoms().get(1);
            assertEquals("test5", a.name());
            assertNull(a.particles());
            assertEquals(5555.55556, a.values().value(), 0);
            assertEquals(now - 70000, a.timestamp(), 0);

            // test scan some particles
            result = continuum.slice(
                    continuum.scan("test5").end(now - 100000).build()
            );
            assertEquals(2, result.atoms().size());

            Map<String, String> map = new HashMap<>();
            map.put("provider", "limelight");
            result = continuum.slice(
                    continuum.scan("test5").dimension(Continuum.Dimension.SPACE).particles(Continuum.particles(map)).end(now - 100000).build()
            );
            assertEquals(1, result.atoms().size());
            assertEquals(5555.001, result.atoms().get(0).values().value(), 0);
        } finally {
            if (continuum != null) continuum.delete();
        }
    }
}

