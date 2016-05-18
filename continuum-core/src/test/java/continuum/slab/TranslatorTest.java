package continuum.slab;

import continuum.Continuum;
import continuum.atom.AtomID;
import continuum.core.slab.AtomTranslator;
import continuum.core.slab.RockSlab;
import continuum.core.slice.AScanner;
import continuum.core.io.file.FileSystemReference;
import continuum.atom.Atom;
import continuum.slice.Function;
import continuum.slice.Scan;
import continuum.slice.Scanner;
import continuum.slice.Slice;
import continuum.util.datetime.Interval;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import static continuum.Continuum.*;

/**
 * Created by zack on 2/11/16.
 */
public class TranslatorTest {

    FileSystemReference reference = new FileSystemReference("/tmp/continuum/test.continuum.TranslatorTest");

    @Test
    public void testSlice() throws Exception {

        Slab slab = new RockSlab("testSlice.0.slab", reference);
        slab.open();

        Map<String, String> particles = new HashMap<>();
        particles.put("tag1", "tagvalue1");
        particles.put("tag2", "2tagvalue2");
        particles.put("tag3", "atagvalue3");

        Map<String, Object> fields = new HashMap<>();
        fields.put("fields", "ffffffff");
        fields.put("fields1", "vfield");
        fields.put("fields4", "fieldv");

        String name = "testSlice";
        reference.child(name).delete();
        AtomTranslator translator = new AtomTranslator(Dimension.SPACE, slab);
        Atom d = satom().name("zack")
                .timestamp(System.currentTimeMillis())
                .particles(particles(particles))
                .fields(fields(fields))
                .value(12346555.0000000000D)
                .build();

        translator.write(d);

        d = satom().name("zack")
                .timestamp(System.currentTimeMillis() - 10)
                .particles(particles(particles))
                .fields(fields(fields))
                .value(98898.124D)
                .build();
        translator.write(d);

        AtomID id = d.ID();
        Atom d2 = translator.read(id);
        assertEquals(d.timestamp(), d2.timestamp());
        assertEquals(98898.124D, d2.values().value(), 0.00001);
        assertEquals(d.ID().string(), d2.ID().string());
        assertEquals(d.ID().name(), d2.ID().name());
        assertArrayEquals(d.ID().bytes(), d2.ID().bytes());
        Scanner scanner = new AScanner();
        try (Iterator iterator = translator.iterator()) {
            scanner.iterator(iterator);
            Scan scan = Continuum
                        .scan().name("zack")
                        .dimension(Dimension.SPACE)
                        .end(Interval.valueOf("1d"))
                        .function(Function.AVG)
                        .build();

            Slice res = scanner.slice(scan);
            Double avg = res.values().value();
            assertEquals((12346555.0000000000D + 98898.124D)/ 2, avg, 0.00001);
        }

        try (Iterator iterator = translator.iterator()) {
            scanner = new AScanner();
            scanner.iterator(iterator);

            List<? extends Atom> atoms = scanner.slice(
                    Continuum.scan().name("zack")
                            .end(Interval.valueOf("1d"))
                            .dimension(Dimension.SPACE)
                            .build())
                    .atoms();

            System.out.println("FOO: " + d2.ID().timestamp());
            atoms.forEach(a -> System.out.println(a.ID().timestamp()));
        }

        slab.close();
        slab.reference().delete();
    }
}