package com.dlvr.continuum.series.datum;

import com.dlvr.continuum.Continuum;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by zack on 2/10/16.
 */
public class TagsTest {
    @Test
    public void testTagsId() {
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "bat");
        Tags tags = Continuum.tags(map);
        String id = tags.ID();
        assertEquals("baz\\x00foo\\x00bat\\x00bar", id);
    }
}