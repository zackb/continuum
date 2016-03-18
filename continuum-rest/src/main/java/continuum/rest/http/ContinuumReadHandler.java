package continuum.rest.http;

import continuum.Continuum;
import continuum.atom.Fields;
import continuum.atom.Particles;
import continuum.REST;
import continuum.rest.http.exception.BadRequestException;
import continuum.rest.http.exception.MethodNotAllowedException;
import continuum.slice.Function;
import continuum.slice.Scan;
import continuum.slice.Slice;
import continuum.util.datetime.Interval;
import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.FileOutputStream;
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

    class ReadRequest {
        public String name;
        public long start = System.currentTimeMillis();
        public long end = System.currentTimeMillis() - Interval.valueOf("1h").millis();
        Interval interval;
        Function function;
        Particles particles;
        Fields fields;
        String[] group;

        @SuppressWarnings("unchecked")
        public ReadRequest(Map<String, Object> data) throws Exception {

            particles = Continuum.particles();
            fields = Continuum.fields();

            for (String key : data.keySet()) {
                Object value = data.get(key);
                switch (key) {
                    case "name":
                        name = (String)value;
                        break;
                    case "start":
                        start = Interval.valueOf((String)value).epoch();
                        break;
                    case "end":
                        end = Interval.valueOf((String)value).epoch();
                        break;
                    case "interval":
                        interval = Interval.valueOf((String)value);
                        break;
                    case "fn":
                        function = Function.fromString((String)value);
                        break;
                    case "group":
                        group = ((String)value).split(",");
                        break;
                    case "fields":
                        fields.putAll((Map<String, Object>)value);
                        break;
                    case "timestamp":
                    case "value":
                    case "values":
                        break;
                    case "particles":
                    default:
                        particles.put(key, (String)value);
                        break;
                }
            }
            check("name", name);
            check("start", start);
            check("end", end);
        }

        private void check(String name, Object value) throws Exception {
            if (value == null) {
                throw new BadRequestException("Parameter: " + name + " is required");
            }
        }
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
