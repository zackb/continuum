package continuum.slice;

import continuum.util.datetime.Interval;

import java.util.List;

/**
 * Describes when and how to down sample data points
 * TODO: better name
 * TODO: better docs
 */
public interface RetentionPolicy {
    List<Interval> intervals();
}
