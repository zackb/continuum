package continuum.core.db;

import continuum.atom.Atom;
import continuum.atom.Values;
import continuum.util.Bytes;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static continuum.Continuum.satom;
import static continuum.Continuum.particles;
import static org.junit.Assert.assertEquals;

/**
 * Created by zack on 2/11/16.
 */
public class AtomTranslatorTest {

    @Test
    public void testEncodeAtom() {
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "bat");
        Atom d = satom().name("foo")
                .particles(particles(map))
                .value(12345.45)
                .build();

        byte[] bytes = Bytes.bytes(d);
        Values values = Bytes.SAtom(bytes).values();
        assertEquals(12345.45, values.value(), 0.000000000001);
        assertEquals(12345.45, values.min(), 0.000000000001);
        assertEquals(12345.45, values.max(), 0.000000000001);
        assertEquals(12345.45, values.sum(), 0.000000000001);
        assertEquals(1, values.count(), 0.000000000001);
    }
}