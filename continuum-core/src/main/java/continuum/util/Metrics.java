package continuum.util;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Timers, counters, gauges
 * Created by zack on 3/14/16.
 */
public class Metrics {

    public static final MetricRegistry registry = new MetricRegistry();

    public static void time(String name, Callable callable) {
        try {
            registry.timer(name).time(callable);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public static void inc(String name) {
        inc(name, 1);
    }

    /**
     * Increment a counter by name
     * @param name counter name
     * @param count amount to increment by
     */
    public static void inc(String name, int count) {
        registry.counter(name).inc(count);
    }

    public static Map<String, Map<String, Number>> report() {
        Map<String, Map<String, Number>> result = new HashMap<>();
        registry.getTimers().forEach((s, timer) -> result.put(s, toReport(timer)));
        registry.getCounters().forEach((s, counter) -> result.put(s, toReport(counter)));
        return result;
    }

    private static Map<String, Number> toReport(Timer timer) {
        Snapshot snapshot = timer.getSnapshot();
        Map<String, Number> report = new HashMap<>();
        report.put("count",             timer.getCount());
        report.put("mean_rate",         timer.getMeanRate());
        report.put("mean",              snapshot.getMean()/1000000);
        report.put("median",            snapshot.getMedian()/1000000);
        report.put("stdev",             snapshot.getStdDev()/1000000);
        report.put("min",               snapshot.getMin()/1000000);
        report.put("max",               snapshot.getMax()/1000000);
        report.put("99percentile",      snapshot.get99thPercentile());
        report.put("1m",                timer.getOneMinuteRate());
        report.put("5m",                timer.getFiveMinuteRate());
        report.put("15m",               timer.getFifteenMinuteRate());
        return report;
    }

    private static Map<String, Number> toReport(Counter counter) {
        Map<String, Number> report = new HashMap<>();
        report.put("count",             counter.getCount());
        return report;
    }
}
