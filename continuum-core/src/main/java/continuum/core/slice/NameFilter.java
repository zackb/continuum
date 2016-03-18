package continuum.core.slice;

import continuum.atom.Atom;
import continuum.slice.Filter;

/**
 * Filter by name for space or time dimension
 * Created by zack on 2/24/16.
 */
public class NameFilter implements Filter {

    private final String prefix;

    public NameFilter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Action filter(Atom atom) {
        if (prefix == null) return Action.CONTINUE;
        // TODO: StartsWith or equals()?
        if (!atom.name().equals(prefix)) return Action.STOP;
        return Action.CONTINUE;
    }
}
