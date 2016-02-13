package com.dlvr.continuum.db.impl;

import com.dlvr.continuum.db.QueryID;
import com.dlvr.continuum.db.datum.Fields;
import com.dlvr.continuum.db.datum.Tags;

/**
 * Time Key Value based QueryID implementation
 * Created by zack on 2/12/16.
 */
public class KVQueryID implements QueryID {

    public KVQueryID(double start, double end, String name, Tags tags, Fields fields, double value) {
    }

    @Override
    public String string() {
        return null;
    }

    @Override
    public byte[] bytes() {
        return new byte[0];
    }
}
