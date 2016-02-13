package com.dlvr.continuum.db.db;

import com.dlvr.continuum.db.AtomID;
import com.dlvr.continuum.db.Iterator;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.core.db.RockDB;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.dlvr.continuum.Continuum.atom;
import static com.dlvr.continuum.Continuum.tags;
import static org.junit.Assert.assertEquals;

/**
 * Created by zack on 2/11/16.
 */
public class IteratorTest {

    FileSystemReference reference = new FileSystemReference("/tmp/continuum/test.com.dlvr.IteratorTest");

    @Test
    public void testIterate() throws Exception {
        String name = "testIterate";
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "bat");

        Map<String, String> map2 = new HashMap<>();
        map2.put("zack", "bar");
        map2.put("fuz", "da'vinci");
        reference.getChild(name).delete();
        RockDB db = new RockDB(name, reference);
        long ts1 = System.currentTimeMillis();
        long ts2 = ts1 + 100;
        db.write(
            atom().name("testiterate")
                    .tags(tags(map))
                    .value(123456.3D)
                    .timestamp(ts1)
                    .build()
        );
        db.write(
                atom().name("testiterate")
                        .tags(tags(map2))
                        .value(12341.01234D)
                        .timestamp(ts2)
                        .build()
        );
        Iterator itr = db.iterator();

        int i = 0;
        itr.seekToFirst();
        do {
            AtomID id = itr.id();
            if (i == 0) {
                assertEquals("testiterate", id.name());
                assertEquals("bar", id.tags().get("foo"));
                assertEquals("bat", id.tags().get("baz"));
                assertEquals(123456.3D, itr.value(), 0.0001);
            } else if (i == 1) {
                assertEquals("testiterate", id.name());
                assertEquals("da'vinci", id.tags().get("fuz"));
                assertEquals("bar", id.tags().get("zack"));
                assertEquals(12341.01234, itr.value(), 0.0001);
            }
            i++;
        } while (itr.next());
        itr.close();
        db.close();
        db.getSlab().getReference().delete();
        assertEquals(2, i);
    }
}