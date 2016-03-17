package com.dlvr.continuum.slice;

import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.db.ID;

/**
 * non-unique ID representing the best place to start a seek from in an LSM tree
 * Created by zack on 2/12/16.
 */
public interface ScanID extends ID {
    String name();
    Particles particles();
}
