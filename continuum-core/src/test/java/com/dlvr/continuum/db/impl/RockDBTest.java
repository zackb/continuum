package com.dlvr.continuum.db.impl;

import com.dlvr.continuum.db.datum.Datum;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.dlvr.continuum.Continuum.datum;
import static com.dlvr.continuum.Continuum.tags;
import static org.junit.Assert.assertEquals;

/**
 * Created by zack on 2/11/16.
 */
public class RockDBTest {

    @Test
    public void testEncodeDatum() {
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "bat");
        Datum d = datum().name("foo")
                .tags(tags(map))
                .value(12345.45)
                .build();

        byte[] bytes = RockDB.value(d);
        double value = RockDB.decodeValue(bytes);
        assertEquals(12345.45, value, 0.000000000001);
    }
}