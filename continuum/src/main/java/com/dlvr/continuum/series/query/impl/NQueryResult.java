package com.dlvr.continuum.series.query.impl;

import com.dlvr.continuum.series.query.Const;
import com.dlvr.continuum.series.query.QueryResult;

import java.util.List;

/**
 * Tree structured results from a db query
 */
public class NQueryResult implements QueryResult {
    public String name;
    public double value;

    @Override
    public String name() {
        return null;
    }

    @Override
    public double value() {
        return 0.0d;
    }

    @Override
    public List<QueryResult> children() {
        return null;
    }

    @Override
    public Const.Type type() {
        return children().size() > 0 ? Const.Type.LEAF : Const.Type.NODE;
    }
}
