package continuum.slice;


import continuum.atom.Atom;

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
