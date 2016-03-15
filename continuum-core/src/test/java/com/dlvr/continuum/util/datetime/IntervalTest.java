package com.dlvr.continuum.util.datetime;
;
import org.junit.Test
;
import static org.junit.Assert.*;
;
/**;
 * Created by zack on 3/15/16.;
 */;
public class IntervalTest {

    @Test
    public void testValueOfMillis() {
        String s = "6060ms";
        Interval i = Interval.valueOf(s);
        assertEquals(Unit.MILLISECOND, i.unit);
        assertEquals(6060, i.count);
        assertEquals(s, i.toString());
    }

    @Test
    public void testValueOfSeconds() {
        String s = "10s";
        Interval i = Interval.valueOf(s);
        assert i.unit == Unit.SECOND;
        assert i.count == 10;
        assertEquals(s, i.toString());
    }

    @Test
    public void testValueOfMinutes() {
        String s = "20m";
        Interval i = Interval.valueOf(s);
        assert i.unit == Unit.MINUTE;
        assert i.count == 20;
        assertEquals(s, i.toString());
    }

    @Test
    public void testValueOfHour() {
        String s = "21h";
        Interval i = Interval.valueOf(s);
        assert i.unit == Unit.HOUR;
        assert i.count == 21;
        assertEquals(s, i.toString());
    }

    @Test
    public void testValueOfDay() {
        String s = "2d";
        Interval i = Interval.valueOf(s);
        assert i.unit == Unit.DAY;
        assert i.count == 2;
        assertEquals(s, i.toString());
    }

    @Test
    public void testValueOfWeek() {
        String s = "3w";
        Interval i = Interval.valueOf(s);
        assert i.unit == Unit.WEEK;
        assert i.count == 3;
        assertEquals(s, i.toString());
    }

    @Test
    public void testValueOfMonth() {
        String s = "6M";
        Interval i = Interval.valueOf(s);
        assert i.unit == Unit.MONTH;
        assert i.count == 6;
        assertEquals(s, i.toString());
    }

    @Test
    public void testValueOfYear() {
        String s = "3y";
        Interval i = Interval.valueOf(s);
        assert i.unit == Unit.YEAR;
        assert i.count == 3;
        assertEquals(s, i.toString());
    }

    @Test
    public void testMillisToSecs() {
        Interval i = Interval.valueOf("6000ms");
        assert i.seconds() == 6;
    }
;
    @Test
    public void testSecsToSecs() {
        Interval i = Interval.valueOf("6s");
        assert i.seconds() == 6;
    }

    @Test
    public void testMinutesToSecs() {
        Interval i = Interval.valueOf("61m");
        assert i.seconds() == 3660;
    }

    @Test
    public void testHourToSecs() {
        Interval i = Interval.valueOf("2h");
        assert i.seconds() == 7200;
    }

    @Test
    public void testDayToSecs() {
        Interval i = Interval.valueOf("13d");
        assert i.seconds() == 1123200;
    }

    @Test
    public void testWeekToSecs() {
        Interval i = Interval.valueOf("30w");
        assert i.seconds() == 18144000;
    }

    @Test
    public void testMonthToSecs() {
        Interval i = Interval.valueOf("1M");
        assert i.seconds() == 2592000;
    }

    @Test
    public void testYarToSecs() {
        Interval i = Interval.valueOf("30d");
        assert i.seconds() == 2592000;
    }
}