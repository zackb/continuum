package com.dlvr.continuum.atom;

import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.datum.ID;

/**
 * Efficient database representation of particles
 * Created by zack on 2/11/16.
 */
public interface ParticlesID extends ID {
    Particles particles();
}
