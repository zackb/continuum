package com.dlvr.continuum.util;

/**
 * "Math" was taken by java.lang
 * Created by zack on 2/13/16.
 */
public class Maths {
    /**
     * Returns random double between start and end inclusive
     * @param min smallest number
     * @param max largest number
     * @return random double between min and max
     */
    public static double randDouble(double min, double max) {
        return min + (double) Math.round(Math.random() * (max - min));
    }

    /**
     * Returns random int between start and end inclusive
     * @param min smallest number
     * @param max largest number
     * @return random int between min and max
     */
    public static int randInt(int min, int max) {
        return min + (int) Math.round(Math.random() * (max - min));
    }
}
