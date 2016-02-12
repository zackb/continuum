package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.util.Bytes;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.dlvr.continuum.Continuum.*;
import static org.junit.Assert.*;

/**
 * Created by zack on 2/11/16.
 */
public class RockDBTest {

    @Test
    public void testEncodeDatum() {
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "bat");
        Datum d = datum("foo")
                .tags(tags(map))
                .value(12345.45)
                .build();

        byte[] bytes = RockDB.value(d);
        double value = RockDB.decodeValue(bytes);
        assertEquals(12345.45, value, 0.000000000001);
    }
}