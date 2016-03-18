package continuum.rest

import continuum.Continuum
import continuum.atom.Atom
import continuum.atom.Values
import continuum.core.io.file.FileSystemReference
import continuum.REST
import continuum.rest.client.Client
import continuum.slice.Scan
import continuum.slice.Slice
import continuum.util.datetime.Interval

/**
 * Created by zack on 2/25/16.
 */
class RESTTests extends GroovyTestCase {

    REST rest
    Continuum continuum

    FileSystemReference baseRef = new FileSystemReference('/tmp/continuum-rest-test/continuum.rest.RESTTests')

    @Override
    void setUp() throws Exception {
        GroovyTestCase.setUp()
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

        junit.framework.TestCase.assertEquals(1, result.atoms().size())
        atom = result.atoms().get(0)
        junit.framework.TestCase.assertEquals(2, atom.particles().size())
        junit.framework.TestCase.assertEquals(2, atom.fields().size())
        groovy.util.GroovyTestCase.assertEquals('foo', atom.particles().get('tag1'))
        groovy.util.GroovyTestCase.assertEquals('bar', atom.particles().get('tag2'))
        groovy.util.GroovyTestCase.assertEquals(123445.2, atom.fields().get('field1'))
        groovy.util.GroovyTestCase.assertEquals('value2', atom.fields().get('2field'))
        groovy.util.GroovyTestCase.assertEquals('fooey1', atom.name())
        Values values = atom.values()
        groovy.util.GroovyTestCase.assertEquals(54.54, values.value())
        groovy.util.GroovyTestCase.assertEquals(54.54, values.min())
        groovy.util.GroovyTestCase.assertEquals(54.54, values.max())
        groovy.util.GroovyTestCase.assertEquals(1, values.count())
        groovy.util.GroovyTestCase.assertEquals(54.54, values.sum())
    }

    @Override
    void tearDown() throws Exception {
        rest.stop()
        continuum.delete()
    }
}
