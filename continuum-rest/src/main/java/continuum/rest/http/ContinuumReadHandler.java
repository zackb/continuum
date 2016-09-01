package continuum.rest.http;

import continuum.REST;
import continuum.rest.http.exception.MethodNotAllowedException;
import continuum.slice.Scan;
import continuum.slice.Slice;
import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Continuum read endpoint
 * Created by zack on 2/23/16.
 */
/// TODO .dot output for graphing
/// TODO .rdf or similar for consumption
public class ContinuumReadHandler implements HttpRequestHandler {
    @Override
    public String getPath() {
        return "/api/1.0/read";
    }

    @Override
    public Object onGet(Map<String, Object> params) throws Exception {
        ReadRequest request = new ReadRequest(params);
        Scan scan = REST.instance().continuum()
                .scan(request.name)
                .start(request.start)
                .end(request.end)
                .interval(request.interval)
                .function(request.function)
                .particles(request.particles)
                .fields(request.fields)
                .group(request.group)
                .limit(request.limit)
                .build();

        return REST.instance()
                .continuum()
                    .slice(scan);
    }

    @Override
    public Object onPost(Map<String, Object> params) throws Exception {
        throw new MethodNotAllowedException("");
    }

    @Override
    public Object onPut(Map<String, Object> params) throws Exception {
        throw new MethodNotAllowedException("");
    }

    @Override
    public Object onDelete() throws Exception {
        throw new MethodNotAllowedException("");
    }

    public static void main(String[] args) throws Exception {

        Slice slice = null;

        ValueFactory factory = SimpleValueFactory.getInstance();

        List<Statement> statements = new ArrayList<>();

        IRI bob = factory.createIRI("http://example.org/bob");
        IRI name = factory.createIRI("http://example.org/name");
        Literal bobsName = factory.createLiteral("Bob");
        statements.add(factory.createStatement(bob, name, bobsName));

        IRI zack = factory.createIRI("http://example.org/zack");
        Literal zacksName = factory.createLiteral("Zack");
        statements.add(factory.createStatement(zack, name, zacksName));

        //FileOutputStream out = new FileOutputStream("/Users/zack/Desktop/file.rdf");
        RDFWriter writer = Rio.createWriter(RDFFormat.RDFXML, System.out);
        writer.startRDF();
        statements.forEach(writer::handleStatement);
        writer.endRDF();
    }
}
