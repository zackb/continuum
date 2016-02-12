package com.dlvr.continuum.db.query.impl;

import com.dlvr.continuum.db.query.Const;
import com.dlvr.continuum.db.query.QueryResult;

import java.util.List;

/**
 * Tree structured results from a db query
 */
public class NQueryResult implements QueryResult {
    public String name;
    public double value;

    @Override
    public String name() {
        return name;
    }

    @Override
    public double value() {
        return value;
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
