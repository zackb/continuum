package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.series.datum.Datum;
import com.dlvr.continuum.series.db.ID;
import com.dlvr.continuum.util.Bytes;

/**
 * Created by zack on 2/11/16.
 */
public class NDatumID implements ID {

    private transient String cachedId;

    public NDatumID(byte[] bytes) {
        this.cachedId = Bytes.String(bytes);
    }

    public NDatumID(Datum datum) {
        cachedId = datum.name() + "\\x00" + datum.tags().ID() + "\\x00" + datum.timestamp();
    }

    @Override
    public String string() {
        return cachedId;
    }

    @Override
    public byte[] bytes() {
        return Bytes.bytes(cachedId);
    }
}
