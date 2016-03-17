package com.dlvr.continuum.rest.client;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.AtomID;
import com.dlvr.continuum.control.Controller;
import com.dlvr.continuum.rest.http.HTTP;
import com.dlvr.continuum.slab.Iterator;
import com.dlvr.continuum.slab.Slab;
import com.dlvr.continuum.slab.Translator;
import com.dlvr.continuum.slice.Scan;
import com.dlvr.continuum.slice.Scanner;
import com.dlvr.continuum.slice.Slice;

import java.util.HashMap;
import java.util.Map;

import static com.dlvr.continuum.Continuum.*;

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
        return Continuum.scan();
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

        if (scan.particles() != null) {
            for (String name : scan.particles().keySet()) {
                Object value = scan.particles().get(name);
                url += "&" + name + "=" + value;
            }
        }

        return HTTP.getJSONObject(url, CSlice.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scanner scanner() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Atom> iterator() {
        throw new UnsupportedOperationException("Can not iterate via HTTP");
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
