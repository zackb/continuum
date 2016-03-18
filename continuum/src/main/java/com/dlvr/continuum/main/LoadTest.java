package com.dlvr.continuum.main;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.slice.Function;
import com.dlvr.continuum.util.Maths;
import com.dlvr.continuum.util.Metrics;
import com.dlvr.continuum.util.datetime.Interval;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Load test
 */
public class LoadTest {

    private Continuum continuum = null;

    LoadTest(Continuum continuum) {
        this.continuum = continuum;
    }

    private final TimerTask timer = new MetricTimer().schedule(() -> System.out.println(Metrics.report()), 5000);

    private final TimerTask reader = new MetricTimer().schedule(() -> {
        try {
            Metrics.time("read", () -> {
                continuum.slice(
                    continuum.scan("series" + Maths.randInt(0, 100))
                            .function(Function.AVG)
                            .interval(Interval.valueOf("1m"))
                            .build()
                );
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    } , 500);

    private static Atom createAtom() {
        return Continuum.satom()
                .name("series" + Maths.randInt(0, 100))
                .value(Maths.randDouble(Double.MIN_VALUE, Double.MAX_VALUE))
                .timestamp(System.currentTimeMillis())
                .build();
    }

    private void stop() {
        timer.cancel();
        reader.cancel();
    }

    private void load(final Continuum continuum, int iterations) {
        for (int i = 0; i < iterations; i++) {
            Metrics.time("write", () -> {
                continuum.write(createAtom());
                return null;
            });
        }
    }

    static class MetricTimer {
        private final Timer t = new Timer();

        public TimerTask schedule(final Runnable r, long delay) {
            final TimerTask task = new TimerTask() { public void run() { r.run(); }};
            t.scheduleAtFixedRate(task, delay, delay);
            return task;
        }
    }

    // 50MB/10M metrics
    public static void perform(Continuum continuum, int iterations) throws Exception {

        LoadTest test = null;
        try {
            test = new LoadTest(continuum);
            test.load(continuum, iterations);
        } finally {
            if (test != null) test.stop();
            //if (delete) ref.delete();
        }
    }
}
