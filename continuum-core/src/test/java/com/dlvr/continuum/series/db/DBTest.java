package com.dlvr.continuum.series.db;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.db.impl.RockDB;
import com.dlvr.continuum.series.query.Function;
import com.dlvr.continuum.series.query.QueryResult;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import static com.dlvr.continuum.Continuum.*;

/**
 * Created by zack on 2/11/16.
 */
public class DBTest {

    FileSystemReference reference = new FileSystemReference("/tmp/continuum/test.com.dlvr.DBTest");

    @Test
    public void testScan() throws Exception {
        Map<String, String> tags = new HashMap<>();
        tags.put("tag1", "tagvalue1");
        tags.put("tag2", "2tagvalue2");
        tags.put("tag3", "atagvalue3");

        Map<String, Object> fields = new HashMap<>();
        fields.put("fields", "ffffffff");
        fields.put("fields1", "vfield");
        fields.put("fields4", "fieldv");

        String name = "testScan";
        reference.getChild(name).delete();
        RockDB db = new RockDB(name, reference);
        Datum d = datum().name("zack")
                .timestamp(System.currentTimeMillis())
                .tags(tags(tags))
                .fields(fields(fields))
                .value(12346555.0000000000D)
                .build();
        db.write(d);

        d = datum().name("zack")
                .timestamp(System.currentTimeMillis() + 10)
                .tags(tags(tags))
                .fields(fields(fields))
                .value(98898.124D)
                .build();
        db.write(d);

        /* TODO
        DatumID id = d.ID();
        d = db.get(id);
        assertEquals(12346555.0000000000D, d.value() , 0.001);
        */
        QueryResult res = db.query(query("test").function(Function.AVG).build());
        double avg = res.value();
        assertEquals((12346555.0000000000D + 98898.124D)/ 2, avg, 0.00001);
        db.close();
        db.getSlab().getReference().delete();
    }
}