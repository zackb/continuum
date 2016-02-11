package com.dlvr.continuum.series.db;

import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.datum.Tags;
import com.dlvr.continuum.series.db.impl.NDatumID;
import com.dlvr.continuum.util.Bytes;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static com.dlvr.continuum.Continuum.*;

/**
 * Created by zack on 2/10/16.
 */
public class IDTests {
    @Test
    public void testTagsIdUnmarshall() {
        byte[] e = { 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r' };
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "bat");
        Tags tags = tags(map);
        assertArrayEquals(e, tags.ID().bytes());
    }

    @Test
    public void testDatumIdMarshall() {
        long ts = 1455219259015L;
        byte[] e = {'z', 'a', 'm', 'e', 0x0, 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 'z', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r' };
        byte[] expected = Bytes.concat(e, (byte)0x0);
        expected = Bytes.concat(expected, Bytes.bytes(ts));
        Map<String, String> map = new HashMap<>();
        map.put("fooz", "bar");
        map.put("baz", "bat");
        Datum d = datum("zame").tags(tags(map)).timestamp(ts).value(1235).build();
        assertArrayEquals(expected, d.ID().bytes());
    }

    @Test
    public void testDatumIdUnMarshall() {
        long ts = 1455219259015L;
        byte[] e = {'z', 'a', 'm', 'e', 0x0, 'b', 'a', 'z', 0x0, 'f', 'o', 'o', 'z', 0x0, 'b', 'a', 't', 0x0, 'b', 'a', 'r' };
        byte[] expected = Bytes.concat(e, (byte)0x0);
        expected = Bytes.concat(expected, Bytes.bytes(ts));
        DatumID id = new NDatumID(expected);
        assertEquals("zame", id.name());
    }
}