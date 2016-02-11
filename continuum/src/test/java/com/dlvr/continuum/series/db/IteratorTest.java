package com.dlvr.continuum.series.db;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
import com.dlvr.continuum.series.db.impl.RockDB;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.dlvr.continuum.Continuum.*;
import static org.junit.Assert.*;

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
            datum("testiterate")
                    .tags(tags(map))
                    .value(1234)
                    .timestamp(ts1)
                    .build()
        );
        db.write(
                datum("testiterate")
                        .tags(tags(map2))
                        .value(12341)
                        .timestamp(ts2)
                        .build()
        );
        Iterator itr = db.iterator();

        int i = 0;
        itr.seekToFirst();
        while (itr.hasNext()) {
            DatumID id = itr.next();
            if (i == 0) {
                assertEquals("testiterate", id.name());
                assertEquals("bar", id.tags().get("foo"));
                assertEquals("bat", id.tags().get("baz"));
            } else if (i == 1){
                assertEquals("testiterate", id.name());
                assertEquals("da'vinci", id.tags().get("fuz"));
                assertEquals("bar", id.tags().get("zack"));
            }
            i++;
        }
        itr.close();
        db.getSlab().getReference().delete();
        assertEquals(2, i);
    }
}