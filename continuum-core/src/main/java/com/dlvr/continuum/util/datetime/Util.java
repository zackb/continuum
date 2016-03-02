package com.dlvr.continuum.util.datetime;

/**
 * Date time utils
 * Created by zack on 2/22/16.
 */
public class Util {
    public static long SECOND   = 1;
    public static long MINUTE   = 60 * SECOND;
    public static long HOUR     = 60 * MINUTE;
    public static long DAY      = 24 * HOUR;

    /**
     * Gets number of minutes ago from now
     * @param minutes to convert
     * @return time in seconds
     */
    public static long minutesAgo(int minutes) {
        return now() - (minutes * MINUTE);
    }

    public static long secondsAgo(int seconds) {
        return now() - (seconds * SECOND);
    }

    public static long hoursAgo(int hours) {
        return now() - (hours * HOUR);
    }

    public static long daysAgo(int days) {
        return now() - (days * DAY);
    }

    /**
     * Current unix epoch time in seconds
     * @return time in seconds
     */
    public static long nowSecs() {
        return System.currentTimeMillis() / 1000;
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    public static long unixTimeAgo(Interval interval) {
        return now() - interval.toSeconds();
    }
}
