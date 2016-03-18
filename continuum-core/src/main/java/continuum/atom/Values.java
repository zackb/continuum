package continuum.atom;

/**
 * Values tuples stored in all atoms
 * (min,max,count,sum,values)
 *
 * Created by zack on 2/24/16.
 */
public interface Values {
    double min();
    double max();
    double count();
    double sum();
    double value();
}
