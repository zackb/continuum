package com.dlvr.continuum;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
import com.dlvr.continuum.series.datum.Datum;
import org.junit.Test;

/**
 * Created by zack on 2/10/16.
 */
public class ContinuumTest {

    FileSystemReference reference = new FileSystemReference("/tmp/continuum/test.com.dlvr.continuum.ContinuumTest");

    @Test
    public void testHello() {
        Continuum.sayHello();
    }

    @Test
    public void testSanity() throws Exception {
        Continuum continuum = null;
        try {
            continuum = Continuum.continuum()
                    .id("testSanity")
                    .base(reference)
                    .open();
        } finally { if (continuum != null) continuum.delete(); }
    }

    @Test
    public void testWriteRead() throws Exception {
        Continuum continuum = null;
        try {
            continuum = Continuum.continuum()
                    .id("testWriteRead")
                    .base(reference)
                    .open();

            Datum datum = Continuum.datum().name("test1")
                                .build();

        } finally { if (continuum != null) continuum.delete(); }
    }
}