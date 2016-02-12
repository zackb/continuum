package com.dlvr.continuum.series.query;

/**
 * Aggregation functions
 */
public enum Function {
    AVG,
    MAX,
    MIN,
    COUNT,
    SUM,
    STD,
    VARIANCE,
    DISTINCT,
    TERMS,
    MAP;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static Function fromString(final String name) {
        for (Function function : values()) {
            if (function.toString().equalsIgnoreCase(name))
                return function;
        }
        return null;
    }
}
