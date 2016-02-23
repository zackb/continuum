package com.dlvr.continuum.util.datetime;

import java.util.concurrent.TimeUnit;

/**
 * Unit of measure
 * Created by zack on 2/22/16.
 */
public enum Unit {
    MILLISECOND("ms"),
    SECOND("s"),
    MINUTE("m"),
    HOUR("h"),
    DAY("d"),
    WEEK("w"),
    MONTH("M"),
    YEAR("y");

    String value;

    Unit(String value) {
        this.value = value;
    }

    public static Unit fromString(String s) {
        switch (s) {
            case "ms":
                return MILLISECOND;
            case "s":
                return SECOND;
            case "m":
                return MINUTE;
            case "h":
                return HOUR;
            case "d":
                return DAY;
            case "w":
                return WEEK;
            case "M":
                return MONTH;
            case "y":
                return YEAR;
        }
        return null;
    }

    public TimeUnit toTimeUnit() {
        switch (this) {
            case MILLISECOND:
                return TimeUnit.MILLISECONDS;
            case SECOND:
                return TimeUnit.SECONDS;
            case MINUTE:
                return TimeUnit.MINUTES;
            case HOUR:
                return TimeUnit.HOURS;
            case DAY:
                return TimeUnit.DAYS;
            default:
                System.err.printf("Can not conver this Unit " + value + " to a java.util.TimeUnit");
        }
        return null;
    }


    @Override
    public String toString() {
        return value;
    }
}
