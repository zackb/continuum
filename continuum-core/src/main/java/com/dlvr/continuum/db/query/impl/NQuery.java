package com.dlvr.continuum.db.query.impl;

import com.dlvr.continuum.db.QueryID;
import com.dlvr.continuum.db.datum.Fields;
import com.dlvr.continuum.db.datum.Tags;
import com.dlvr.continuum.db.impl.NQueryID;
import com.dlvr.continuum.db.query.Function;
import com.dlvr.continuum.db.query.Query;

import java.util.concurrent.TimeUnit;

/**
 * Base Query implementation
 */
public class NQuery implements Query {
    public String name;
    public Function function;
    public long start;
    public long end;
    public TimeUnit interval;
    public Tags tags;
    public Fields fields;
    public double value;

    @Override
    public QueryID ID() {
        return new NQueryID(name, tags);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Tags tags() {
        return tags;
    }

    @Override
    public Fields fields() {
        return fields;
    }

    @Override
    public Function function() {
        return function;
    }

    @Override
    public long start() {
        return start;
    }

    @Override
    public long end() {
        return end;
    }

    @Override
    public TimeUnit interval() {
        return interval;
    }
}
