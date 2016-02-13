package com.dlvr.continuum.db;

import com.dlvr.continuum.atom.Particles;

/**
 * Created by zack on 2/11/16.
 */
public interface AtomID extends ID {
    String name();
    Particles particles();
    long timestamp();
}
