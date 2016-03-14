package com.dlvr.continuum.rest

import com.dlvr.continuum.Continuum
import com.dlvr.continuum.atom.Atom
import com.dlvr.continuum.atom.Values
import com.dlvr.continuum.core.io.file.FileSystemReference
import com.dlvr.continuum.db.slice.Scan
import com.dlvr.continuum.db.slice.Slice
import com.dlvr.continuum.rest.client.Client
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
        baseRef.delete()
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

        Atom atom = client.atom()
                        .name('fooey1')
                        .particles(Continuum.particles(
                                'tag1', 'foo',
                                'tag2', 'bar'
                        ))
                        .fields(Continuum.fields('field1', 123445.2, '2field', 'value2'))
                        .value(54.54)
                        .timestamp(timestamp)
                        .build()
        client.write(atom)

        Scan scan = continuum.scan("fooey1")
                        .start(System.currentTimeMillis())
                        .end(Interval.valueOf("10d"))
                        .build()

        Slice result = client.slice(scan)

        assertEquals(1, result.atoms().size())
        atom = result.atoms().get(0)
        assertEquals(2, atom.particles().size())
        assertEquals(2, atom.fields().size())
        assertEquals('foo', atom.particles().get('tag1'))
        assertEquals('bar', atom.particles().get('tag2'))
        assertEquals(123445.2, atom.fields().get('field1'))
        assertEquals('value2', atom.fields().get('2field'))
        assertEquals('fooey1', atom.name())
        Values values = atom.values()
        assertEquals(54.54, values.value())
        assertEquals(54.54, values.min())
        assertEquals(54.54, values.max())
        assertEquals(1, values.count())
        assertEquals(54.54, values.sum())
    }

    @Override
    void tearDown() throws Exception {
        rest.stop()
        continuum.delete()
    }
}
