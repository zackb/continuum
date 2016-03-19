package continuum.core.slice;

import continuum.atom.Atom;
import continuum.slice.Filter;

/**
 * Combine multiple filters
 * Created by zack on 2/24/16.
 */
public class AndFilter implements Filter {
    private final Filter[] filters;
    public AndFilter(Filter... filters) {
        this.filters = filters;
    }
    @Override
    public Action filter(Atom atom) {

        Action action = Action.CONTINUE;
        for (Filter filter : filters) {
            Action a = filter.filter(atom);
            if (a == Action.STOP)
                return Action.STOP;
            if (a == Action.SKIP)
                action = Action.SKIP;
        }
        return action;
    }
}
