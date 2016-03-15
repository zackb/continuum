package com.dlvr.continuum.util.datetime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date Time utils
 * Created by zack on 2/22/16.
 */
public class Interval {

    /**
     * The unit of time
     */
    public final Unit unit;

    /**
     * The number of units
     */
    public final long count;

    private Interval(Unit unit, long count) {
        // assume unix time in seconds
        if (unit == null) {
            this.unit = Unit.MILLISECOND;
            this.count = Util.now() - count;
        } else {
            this.unit = unit;
            this.count = count;
        }
    }

    private static final Pattern pattern = Pattern.compile("(\\d+)(.*)");

    public static Interval valueOf(long millis) {
        return new Interval(Unit.MILLISECOND, millis);
    }

    public static Interval valueOf(String s) {
        Interval result = null;
        Matcher m = pattern.matcher(s);
        if (m.find()) {
            long count = Long.parseLong(m.group(1));
            Unit unit = Unit.fromString(m.group(2));
            result = new Interval(unit, count);
        }
        return result;
    }

    public static Interval valueOf(Unit unit, long count) {
        return new Interval(unit, count);
    }

    public long seconds() {
        long secs = count;
        switch (unit) {
            case MILLISECOND:
                secs = count / 1000L;
                break;
            case SECOND:
                break;
            case MINUTE:
                secs = count * 60;
                break;
            case HOUR:
                secs = count * 60 * 60;
                break;
            case DAY:
                secs = count * 60 * 60 * 24;
                break;
            case WEEK:
                secs = count * 60 * 60 * 24 * 7;
                break;
            case MONTH:
                secs = count * 60 * 60 * 24 * 30;
                break;
            case YEAR:
                secs = count * 60 * 60 * 24 * 365;
                break;
        }
        return secs;
    }

    public long millis() {
        long millis = count;
        switch (unit) {
            case MILLISECOND:
                break;
            case SECOND:
                millis = count * 1000;
                break;
            case MINUTE:
                millis = count * 1000 * 60;
                break;
            case HOUR:
                millis = count * 1000 * 60 * 60;
                break;
            case DAY:
                millis = count * 1000 * 60 * 60 * 24;
                break;
            case WEEK:
                millis = count * 1000 * 60 * 60 * 24 * 7;
                break;
            case MONTH:
                millis = count * 1000 * 60 * 60 * 24 * 7 * 30;
                break;
            case YEAR:
                millis = count * 1000 * 60 * 60 * 24 * 7 * 365;
                break;
        }
        return millis;
    }

    public long epoch() {
        return Util.now() - millis();
    }

    @Override
    public String toString() {
        return count + unit.toString();
    }
}
