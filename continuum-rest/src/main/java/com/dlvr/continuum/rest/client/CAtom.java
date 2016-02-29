package com.dlvr.continuum.rest.client;

import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Fields;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.atom.Values;
import com.dlvr.continuum.db.AtomID;

/**
 * Basic atom DTO
 * Created by zack on 2/26/16.
 */
public class CAtom implements Atom {

    @Override
    public AtomID ID() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public Particles particles() {
        return null;
    }

    @Override
    public Fields fields() {
        return null;
    }

    @Override
    public Values values() {
        return null;
    }

    @Override
    public Long timestamp() {
        return null;
    }
}
