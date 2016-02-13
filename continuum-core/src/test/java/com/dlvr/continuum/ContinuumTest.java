package com.dlvr.continuum;

import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.atom.Atom;
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

            Atom atom = Continuum.atom().name("test1")
                                .build();

        } finally { if (continuum != null) continuum.delete(); }
    }
}