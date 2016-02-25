package com.dlvr.continuum.db;

import com.dlvr.continuum.atom.Particles;

/**
 * non-unique ID representing the best place to start a seek from in an LSM tree
 * Created by zack on 2/12/16.
 */
public interface SliceID extends ID {
    String name();
    Particles particles();
}
