package com.dlvr.continuum.slice;


import com.dlvr.continuum.atom.Atom;

/**
 * Time scan filter engines
 * Created by zack on 2/12/16.
 */
public interface Filter {
    Action filter(Atom atom);
    enum Action {
        CONTINUE,SKIP,STOP
    }
}
