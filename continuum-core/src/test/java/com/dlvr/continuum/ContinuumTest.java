package com.dlvr.continuum;

import com.dlvr.continuum.io.file.impl.FileSystemReference;
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
        Continuum continuum = Continuum.continuum()
                                .id("testSanity")
                                .base(reference)
                                .open();
        continuum.delete();
    }
}