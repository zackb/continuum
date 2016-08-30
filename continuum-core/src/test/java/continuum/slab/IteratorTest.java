package continuum.slab;

import continuum.Continuum;
import continuum.atom.AtomID;
import continuum.atom.Particles;
import continuum.core.slab.RockSlab;
import continuum.core.io.file.FileSystemReference;
import continuum.core.slab.AtomTranslator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static continuum.Continuum.particles;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by zack on 2/11/16.
 */
public class IteratorTest {

    FileSystemReference reference = new FileSystemReference("/tmp/continuum/test.IteratorTest");

    @Test
    public void testIterate() throws Exception {

        reference.delete();
        Slab slab = new RockSlab("testIterate.0.slab", reference);
        slab.open();

        String name = "testIterate";
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "bat");

        Map<String, String> map2 = new HashMap<>();
        map2.put("zack", "bar");
        map2.put("fuz", "da'vinci");
        reference.child(name).delete();

        AtomTranslator translator = new AtomTranslator(Continuum.Dimension.SPACE, slab);

        long ts1 = System.currentTimeMillis();
        long ts2 = ts1 + 100;

        translator.write(
            Continuum.satom().name("testiterate")
                    .particles(particles(map))
                    .value(123456.3D)
                    .timestamp(ts1)
                    .build()
        );

        translator.write(
                Continuum.satom().name("testiterate")
                        .particles(particles(map2))
                        .value(12341.01234D)
                        .timestamp(ts2)
                        .build()
        );
        Iterator itr = translator.iterator();

        int i = 0;
        itr.seekToFirst();
        do {
            AtomID id = itr.ID();
            if (i == 0) {
                assertEquals("testiterate", id.name());
                Particles particles = id.particles();
                assertEquals("da'vinci", particles.get("fuz"));
                assertEquals("bar", particles.get("zack"));
                assertEquals(12341.01234, itr.values().value(), 0.0001);
                assertEquals(id.timestamp(), ts2);
            } else if (i == 1) {
                assertEquals("testiterate", id.name());
                assertEquals("bar", id.particles().get("foo"));
                assertEquals("bat", id.particles().get("baz"));
                assertEquals(123456.3D, itr.values().value(), 0.0001);
                assertEquals(id.timestamp(), ts1);
            }
            i++;
        } while (itr.next());
        itr.close();
        slab.close();
        slab.reference().delete();
        assertEquals(2, i);
    }

    @Test
    public void testTtl() throws Exception {

        int ttl = 2; // 2 second ttl

        reference.delete();
        RockSlab slab = new RockSlab("testTtl.0.slab", reference, ttl);

        slab.open();

        slab.write("foo".getBytes(), "bar".getBytes());

        assertArrayEquals("bar".getBytes(), slab.read("foo".getBytes()));

        TimeUnit.SECONDS.sleep(ttl + 1);

        slab.compactRange();

        assertEquals(null, slab.read("foo".getBytes()));

        slab.close();
        slab.reference().delete();
    }
}
