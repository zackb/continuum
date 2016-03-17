package com.dlvr.continuum.atom;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The particles represented in a time translator
 */
public interface Particles extends Map<String, String>, Serializable {
    String put(String key, String value);
    String get(String key);
    List<String> names();
    ParticlesID ID();
}