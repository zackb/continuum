package com.dlvr.continuum.datum.slice;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.slice.Function;
import com.dlvr.continuum.slice.Slice;
import com.dlvr.continuum.util.Maths;
import com.dlvr.continuum.util.datetime.Interval;
import org.junit.Test;

import static org.junit.Assert.*;
import static com.dlvr.continuum.Continuum.*;

/**
 * Scan and slice tests
 *
 * Created by zack on 3/15/16.
 */
public class ScanTest {

    FileSystemReference reference = new FileSystemReference("/tmp/continuum/test.com.dlvr.continuum.translator.slice.ScanTest");

    @Test
    public void testGroup() throws Exception {
        Continuum continuum = null;
        try {
            continuum =
                    continuum()
                        .name("testGroup")
                        .dimension(Dimension.SPACE)
                        .base(reference)
                        .open();


            // build temperature translator
            buildTempDB(continuum);

            // average over two days
            Slice slice = continuum.slice(
                    continuum.scan("temp")
                            .function(Function.AVG)
                            .end(Interval.valueOf("2d"))
                        .build());

            assertEquals(7, slice.values().count(), 0);
            assertEquals(98.2714, slice.values().value(), 0);
            assertEquals(61.5, slice.values().min(), 0);
            assertEquals(120.9999, slice.values().max(), 0);
            assertEquals(687.8998, slice.values().sum(), 0);
            assertNull(slice.slices());

            // average over two days grouped by country, state
            slice = continuum.slice(
                    continuum.scan("temp")
                            .function(Function.AVG)
                            .group("country", "state")
                            .end(Interval.valueOf("2d"))
                            .build());

            // US
            assertEquals(1, slice.slices().size());
            Slice us = slice.slice("temp:us");
            assertEquals(2, us.slices().size());
            assertEquals(98.2714, slice.values().value(), 0);
            assertEquals(61.5, slice.values().min(), 0);
            assertEquals(120.9999, slice.values().max(), 0);
            assertEquals(687.8998, slice.values().sum(), 0);

            // California
            Slice ca = us.slice("temp:us:ca");
            //assertEquals(2, ca.slices().size());
            assertEquals(111.87998, ca.values().value(), 0);
            assertEquals(99.1, ca.values().min(), 0);
            assertEquals(120.9999, ca.values().max(), 0);
            assertEquals(559.3999, ca.values().sum(), 0);
            assertEquals(5, ca.values().count(), 0);

            // Oregon
            Slice or = us.slice("temp:us:or");
            assertEquals(64.24995, or.values().value(), 0);
            assertEquals(61.5, or.values().min(), 0);
            assertEquals(66.9999, or.values().max(), 0);
            assertEquals(128.4999, or.values().sum(), 0);

        } finally {
            if (continuum != null) continuum.delete();
        }
    }

    private static void buildTempDB(Continuum continuum) throws Exception {
        long oneDay = Interval.valueOf("1d").millis();
        long now = System.currentTimeMillis() - oneDay;

        ///// LAX
        continuum.write(
                continuum
                        .atom("temp")
                        .value(99.1)
                        .timestamp(now)
                        .particles(
                                "country", "us",
                                "state","ca",
                                "city", "lax"
                        ).build());



        continuum.write(
                continuum
                        .atom("temp")
                        .value(115.1)
                        .timestamp(now + Maths.randInt(2, 100))
                        .particles(
                                "country", "us",
                                "state","ca",
                                "city", "lax"
                        ).build());

        continuum.write(
                continuum
                        .atom("temp")
                        .value(107.1)
                        .timestamp(now + Maths.randInt(100, 200))
                        .particles(
                                "country", "us",
                                "state","ca",
                                "city", "lax"
                        ).build());


        ///// LAX


        //// PSP
        continuum.write(
                continuum
                        .atom("temp")
                        .value(117.1)
                        .timestamp(now + Maths.randInt(0, 100))
                        .particles(
                                "country", "us",
                                "state","ca",
                                "city", "psp"
                        ).build());

        continuum.write(
                continuum
                        .atom("temp")
                        .value(120.9999)
                        .timestamp(now + Maths.randInt(100, 200))
                        .particles(
                                "country", "us",
                                "state","ca",
                                "city", "psp"
                        ).build());
        //// PSP



        /// PDX
        continuum.write(
                continuum
                        .atom("temp")
                        .value(61.5)
                        .timestamp(now)
                        .particles(
                                "country", "us",
                                "state","or",
                                "city", "pdx"
                        ).build());

        continuum.write(
                continuum
                        .atom("temp")
                        .value(66.9999)
                        .timestamp(now + Maths.randInt(100, 200))
                        .particles(
                                "country", "us",
                                "state","or",
                                "city", "pdx"
                        ).build());
        /// PDX

        /// BOGUS!
        continuum.write(
                continuum
                        .atom("" + now)
                        .value(Double.MAX_VALUE - 10)
                        .timestamp(now + Maths.randInt(-100, 10000))
                        .particles(
                                "country", "BABABOOOOOOEY",
                                "state","orx",
                                "city", "pdxxx"
                        ).build());

        continuum.write(
                continuum
                        .atom("bababooey")
                        .value(Double.MIN_VALUE + 10)
                        .timestamp(now + Maths.randInt(-100, 10000))
                        .particles(
                                "country", "fla fla flo hi",
                                "state","13",
                                "city", "pdxxx"
                        ).build());
        /// BOGUS!
    }
}