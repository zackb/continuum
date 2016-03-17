package com.dlvr.continuum.atom;

import com.dlvr.continuum.ID;

/**
 * Created by zack on 2/11/16.
 */
public interface AtomID extends ID {
    String name();
    Particles particles();
    long timestamp();
}
