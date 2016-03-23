package continuum.rest.client;

import continuum.Continuum;
import continuum.atom.Atom;
import continuum.atom.AtomID;
import continuum.control.Controller;
import continuum.slab.Iterator;
import continuum.slab.Slab;
import continuum.slab.Translator;
import continuum.slice.Scan;
import continuum.slice.Scanner;
import continuum.slice.Slice;
import continuum.rest.http.HTTP;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static continuum.Continuum.*;

/**
 * REST client wrapper
 * Created by zack on 2/23/16.
 */
public class Client implements Controller, Translator<Atom> {

    private final String baseUrl;

    public Client() {
        this("localhost");
    }

    public Client(String host) {
        this(host, 1337);
    }

    //TODO: HttpSlab?
    public Client(String host, int port) {
        baseUrl = "http://" + host + ":" + port + "/api/1.0";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Continuum.AtomBuilder atom() {
        return new AtomBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Continuum.AtomBuilder atom(String name) {
        return atom().name(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScanBuilder scan(String name) {
        return Continuum.scan().name(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Translator<Atom> translator() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Atom atom) throws Exception {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> fields = new HashMap<>();
        data.put("name", atom.name());
        data.put("value", atom.values().value());
        data.put("timestamp", atom.timestamp());

        if (atom.particles() != null)
            for (String key : atom.particles().keySet())
                data.put(key, atom.particles().get(key));

        if (atom.fields() != null)
            for (String key : atom.fields().keySet())
                fields.put(key, atom.fields().get(key));

        if (fields.size() > 0)
            data.put("fields", fields);

        HTTP.postJSON(baseUrl + "/write", data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Atom get(AtomID atomID) throws Exception {
        return read(atomID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Atom read(AtomID atomID) throws Exception {
        throw new UnsupportedOperationException("Mer");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slice slice(Scan scan) throws Exception {

        String url = baseUrl + "/read?name=" + scan.name();
        url += "&start=" + scan.start();
        url += "&end=" + scan.end();
        if (scan.function() != null)
            url += "&fn=" + scan.function().name();

        if (scan.interval() != null)
            url += "&interval=" + scan.interval().toString();

        if (scan.particles() != null) {
            for (String name : scan.particles().keySet()) {
                Object value = scan.particles().get(name);
                url += "&" + name + "=" + value;
            }
        }

        if (scan.fields() != null) {
            String fields = "";
            for (String name : scan.fields().keySet()) {
                Object value = scan.fields().get(name);
                fields += name + ":" + value;
            }
            url += "&fields=" + fields;
        }

        if (scan.groups() != null) {
            url += "&group=" + String.join(",", scan.groups());
        }

        return HTTP.getJSONObject(url, CSlice.class);
    }

    @Override
    public Stream<Slice> stream(Scan scan) throws Exception {
        throw new Exception("HTTP Streaming not yet supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scanner scanner() {
        System.err.println("Client.scanner() not implemented!!!");
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Atom> iterator() {
        throw new UnsupportedOperationException("Can not iterate via HTTP");
    }

    @Override
    public Iterator<Atom> iterator(boolean b) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Slab slab() {
        return null;
    }

    public class AtomBuilder extends Continuum.AtomBuilder {

        private AtomBuilder() { }

        public AtomBuilder name(String name) {
            super.name(name);
            return this;
        }

        @Override
        public Atom build() {
            return new CAtom(name, particles, timestamp, fields, values);
        }
    }
}
