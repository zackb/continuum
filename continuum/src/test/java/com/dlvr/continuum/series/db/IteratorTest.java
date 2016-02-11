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

    FileSystemReference reference = new FileSystemReference("/Users/zack/Desktop/DBBB");

    @Test
    public void testIterate() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "bat");
        RockDB db = new RockDB("fooe", reference);
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
                        .tags(tags(map))
                        .value(12341)
                        .timestamp(ts2)
                        .build()
        );
        Iterator itr = db.iterator();

        System.out.println("Ts1: " + ts1);
        System.out.println("Ts2: " + ts2);

        int i = 0;
        itr.seekToFirst();
        while (itr.hasNext()) {
            i++;
            DatumID id = itr.next();
            System.out.println(id.name());
            System.out.println(id.timestamp());
        }
        itr.close();
        db.getSlab().getReference().delete();
        assertEquals(2, i);
    }
}