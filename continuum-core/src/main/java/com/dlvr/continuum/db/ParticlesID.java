package com.dlvr.continuum.db;

import com.dlvr.continuum.atom.Particles;

/**
 * Efficient database representation of particles
 * Created by zack on 2/11/16.
 */
public interface ParticlesID extends ID {
    Particles particles();
}
