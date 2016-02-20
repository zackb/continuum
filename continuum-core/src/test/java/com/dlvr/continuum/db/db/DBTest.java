package com.dlvr.continuum.db.db;

import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.core.db.RockDB;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.SliceResult;
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
    public void testSlice() throws Exception {
        Map<String, String> particles = new HashMap<>();
        particles.put("tag1", "tagvalue1");
        particles.put("tag2", "2tagvalue2");
        particles.put("tag3", "atagvalue3");

        Map<String, Object> fields = new HashMap<>();
        fields.put("fields", "ffffffff");
        fields.put("fields1", "vfield");
        fields.put("fields4", "fieldv");

        String name = "testSlice";
        reference.getChild(name).delete();
        RockDB db = new RockDB(Dimension.TIME, name, reference);
        Atom d = satom().name("zack")
                .timestamp(System.currentTimeMillis())
                .particles(particles(particles))
                .fields(fields(fields))
                .value(12346555.0000000000D)
                .build();
        db.write(d);

        d = satom().name("zack")
                .timestamp(System.currentTimeMillis() + 10)
                .particles(particles(particles))
                .fields(fields(fields))
                .value(98898.124D)
                .build();
        db.write(d);

        /* TODO
        AtomID id = d.ID();
        d = db.get(id);
        assertEquals(12346555.0000000000D, d.value() , 0.001);
        */
        SliceResult res = db.slice(slice("test").function(Function.AVG).build());
        Map<Long, Double> avg = res.value();
        assert avg.size() == 1;
        assertEquals((12346555.0000000000D + 98898.124D)/ 2, avg.values().iterator().next(), 0.00001);
        db.close();
        db.getSlab().getReference().delete();
    }
}