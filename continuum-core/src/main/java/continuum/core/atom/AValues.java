package continuum.core.atom;

import continuum.atom.Values;

/**
 * Base values implementation
 * Created by zack on 2/24/16.
 */
public class AValues implements Values {

    public double min = 0;
    public double max = 0;
    public double count = 0;
    public double sum = 0;
    public double value = 0;

    @Override
    public double min() {
        return min;
    }

    @Override
    public double max() {
        return max;
    }

    @Override
    public double count() {
        return count;
    }

    @Override
    public double sum() {
        return sum;
    }

    @Override
    public double value() {
        return value;
    }
}
