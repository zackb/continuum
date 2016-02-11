package com.dlvr.continuum.series.db;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.db.impl.RockDB;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.dlvr.continuum.Continuum.*;
import static org.junit.Assert.*;

/**
 * Created by zack on 2/11/16.
 */
public class DBTest {
    FileSystemReference reference = new FileSystemReference("/Users/zack/Desktop/DBBB");
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

        RockDB db = new RockDB("fooe", reference);
        Datum d = datum("zack")
                .timestamp(System.currentTimeMillis())
                .tags(tags(tags))
                .fields(fields(fields))
                .value(1234)
                .build();
        db.write(d);

        db.query(null);
        db.getSlab().getReference().delete();
    }
}