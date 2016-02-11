package com.dlvr.continuum.util;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.util.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by zack on 2/10/16.
 */
public class BytesTest {
    @Test
    public void testSplitByteArray() {
        byte[] bytes = { 0x1, 0x2 , 0xF, 0x4, 0x5, 0xF, 0x6, 0x7, 0x8, 0x9, 0xF, 0x1, 0xF };
        byte[][] split = Bytes.split(bytes, (byte)0xF);
        assert split.length == 4;

        assert split[0].length == 2;
        assert split[0][0] == 0x1;
        assert split[0][1] == 0x2;
        assert split[1].length == 2;
        assert split[1][0] == 0x4;
        assert split[1][1] == 0x5;
        assert split[2].length == 4;
        assert split[2][0] == 0x6;
        assert split[2][1] == 0x7;
        assert split[2][2] == 0x8;
        assert split[2][3] == 0x9;
        assert split[3].length == 1;
        assert split[3][0] == 0x1;
    }

    @Test
    public void testSerDe() {
        Map<String, String> tags = new HashMap<>();
        tags.put("tag1", "tagvalue1");
        tags.put("tag2", "2tagvalue2");
        tags.put("tag3", "atagvalue3");

        Map<String, Object> fields = new HashMap<>();
        fields.put("field1", "fieldvalue1");
        fields.put("2field2", 74190D);

        long ts = System.currentTimeMillis();
        Datum datum = Continuum.datum("test1")
                .tags(Continuum.tags(tags))
                .fields(Continuum.fields(fields))
                .timestamp(ts)
                .value(54D)
                .build();
        byte[] bytes = Bytes.bytes(datum);

        datum = Bytes.Datum(bytes);
        assertEquals("test1", datum.name());
        assertEquals(3, datum.tags().size());
        assertEquals("tagvalue1", datum.tags().get("tag1"));
        assertEquals(54D, datum.value(), 0.00000001d);
    }

}