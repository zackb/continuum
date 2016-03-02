package com.dlvr.continuum.rest.client;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.db.AtomID;
import com.dlvr.continuum.db.DB;
import com.dlvr.continuum.db.Iterator;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.db.slice.SliceResult;
import com.dlvr.continuum.util.datetime.Interval;
import com.dlvr.net.http.HTTP;

import java.util.HashMap;
import java.util.Map;

/**
 * REST client wrapper
 * Created by zack on 2/23/16.
 */
public class Client implements DB {

    private final String baseUrl;

    public Client() {
        this("localhost");
    }

    public Client(String host) {
        this(host, 1337);
    }

    public Client(String host, int port) {
        baseUrl = "http://" + host + ":" + port + "/api/1.0";
    }

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

    @Override
    public Atom get(AtomID atomID) throws Exception {
        throw new UnsupportedOperationException("Mer");
    }

    @Override
    public SliceResult slice(Slice slice) throws Exception {

        String url = baseUrl + "/read?name=" + slice.name();
        url += "&start=" + slice.start();
        url += "&end=" + slice.end();

        if (slice.particles() != null) {
            for (String name : slice.particles().keySet()) {
                Object value = slice.particles().get(name);
                url += "&" + name + "=" + value;
            }
        }

        return HTTP.getJSONObject(url, CSliceResult.class);
    }


    public Continuum.AtomBuilder atom() {
        return new AtomBuilder();
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException("Can not iterate via HTTP");
    }

    @Override
    public void close() throws Exception {

    }

    public class AtomBuilder extends Continuum.AtomBuilder {

        private AtomBuilder() { }

        public AtomBuilder name(String name) {
            super.name(name);
            return this;
        }

        public Atom build() {
            return new CAtom(name, particles, timestamp, fields, values);
        }
    }
}
