package com.dlvr.continuum.rest

import com.dlvr.continuum.Continuum
import com.dlvr.continuum.atom.Atom
import com.dlvr.continuum.core.io.file.FileSystemReference
import com.dlvr.continuum.db.slice.Slice
import com.dlvr.continuum.db.slice.SliceResult
import com.dlvr.continuum.rest.client.Client
import com.dlvr.continuum.util.JSON
import com.dlvr.continuum.util.datetime.Interval
import org.junit.Test

/**
 * Created by zack on 2/25/16.
 */
class RESTTests extends GroovyTestCase {

    REST rest
    Continuum continuum

    FileSystemReference baseRef = new FileSystemReference('/tmp/continuum-rest-test/com.dlvr.continuum.rest.RESTTests')

    @Override
    void setUp() throws Exception {
        super.setUp()
        continuum = Continuum.continuum()
            .name('RESTTests')
            .base(baseRef)
            .dimension(Continuum.Dimension.SPACE)
            .open()
        rest = new REST(continuum, 13370)
        rest.start()
    }

    void testSimple() {

        Client client = REST.client('localhost', 13370)

        Long timestamp = System.currentTimeMillis()

        Atom atom = continuum.atom()
                        .name('fooey1')
                        .particles(Continuum.particles(
                                'tag1', 'foo'
                                //'tag2', 'bar'
                        ))
                        .value(54.54)
                        .timestamp(timestamp)
                        .build()
        client.write(atom)

        Slice slice = continuum.slice("fooey1")
                        .start(System.currentTimeMillis())
                        .end(Interval.valueOf("10d"))
                        .build()
        SliceResult result = client.slice(slice)
        println JSON.encode(result)
    }

    @Override
    void tearDown() throws Exception {
        rest.stop()
        continuum.delete()
    }
}