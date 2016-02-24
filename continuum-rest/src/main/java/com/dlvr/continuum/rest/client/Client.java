package com.dlvr.continuum.rest.client;

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

    private final String host;
    private final int port;
    private final String baseUrl;

    public Client() {
        this("localhost");
    }

    public Client(String host) {
        this(host, 1337);
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        baseUrl = "http://" + host + ":" + port + "/api/1.0";
    }

    @Override
    public void write(Atom atom) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("name", atom.name());
        data.put("value", atom.value());
        data.put("timestamp", atom.timestamp());
        for (String key : atom.particles().keySet()) {
            data.put(key, atom.particles().get(key));
        }
        HTTP.postJSON(baseUrl + "/write", atom);
    }

    @Override
    public Atom get(AtomID atomID) throws Exception {
        throw new UnsupportedOperationException("Mer");
    }

    @Override
    public SliceResult slice(Slice slice) throws Exception {

        String url = baseUrl + "/read?name=" + slice.name();
        url += "&start=" + Interval.valueOf(slice.start() + "ms");
        url += "&end=" + Interval.valueOf(slice.end() + "ms");

        for (String name : slice.particles().keySet()) {
            Object value = slice.particles().get(name);
            url += "&" + name + "=" + value;
        }
        return HTTP.getJSONObject(url, SliceResult.class);
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException("Can not iterate via HTTP");
    }

    @Override
    public void close() throws Exception {

    }

    public static void main(String[] args) {

    }
}
