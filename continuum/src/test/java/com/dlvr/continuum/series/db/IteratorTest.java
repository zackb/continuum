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
        db.write(
            datum("testiterate")
                    .tags(tags(map))
                    .value(1234)
                    .timestamp(System.currentTimeMillis())
            .build()
        );
        Iterator itr = db.iterator();

        int i = 0;
        itr.seekToFirst();
        while (itr.hasNext()) {
            i++;
            ID id = itr.next();
            System.out.println(id.string());
        }
        itr.close();
        db.getSlab().getReference().delete();
        assertEquals(1, i);
    }

}