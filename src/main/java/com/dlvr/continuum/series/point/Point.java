package com.dlvr.continuum.series.point;

/**
 * Time series data point
 */
public interface Point {
    /**
     * Measurement name
     */
    String name();

    /**
     * The tags associated with this time series data point.
     * Under the hood tags.size() * name time series are created
     * Only use tags for data which needs to be queried scalably
     */
    Tags tags();

    /**
     * Fields associated with this tagged, measurement's data point
     * Use tags for meta data which does not need to be queried scalably
     */
    Fields fields();

    /**
     * Non-empty double value of this data point
     */
    double value();

    /**
     * The timestamp of the point expressed as millisecond epoch
     */
    Long timestamp();
}
