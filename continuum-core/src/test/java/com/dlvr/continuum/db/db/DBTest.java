package com.dlvr.continuum.db.db;

import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.core.db.RockDB;
import com.dlvr.continuum.db.scan.Function;
import com.dlvr.continuum.db.scan.ScanResult;
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
        Map<String, String> particles = new HashMap<>();
        particles.put("tag1", "tagvalue1");
        particles.put("tag2", "2tagvalue2");
        particles.put("tag3", "atagvalue3");

        Map<String, Object> fields = new HashMap<>();
        fields.put("fields", "ffffffff");
        fields.put("fields1", "vfield");
        fields.put("fields4", "fieldv");

        String name = "testScan";
        reference.getChild(name).delete();
        RockDB db = new RockDB(Dimension.SERIES, name, reference);
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
        ScanResult res = db.scan(scan("test").function(Function.AVG).build());
        double avg = res.value();
        assertEquals((12346555.0000000000D + 98898.124D)/ 2, avg, 0.00001);
        db.close();
        db.getSlab().getReference().delete();
    }
}