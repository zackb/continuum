package com.dlvr.continuum.main;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.db.slice.Function;
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
                continuum.db().slice(
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
                continuum.db().write(createAtom());
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

    public static void load(String dataDir) throws Exception {
        load(dataDir, 2000000);
    }

    public static void load(String dataDir, int iterations) throws Exception {
        load(dataDir, iterations, true);
    }

    // 50MB/10M metrics
    public static void load(String dataDir, int iterations, boolean delete) throws Exception {
        FileSystemReference ref = new FileSystemReference(dataDir);

        Continuum.Builder builder =
                Continuum.continuum()
                    .name("com.dlvr.continuum.main.LoadTest")
                        .base(ref);

        LoadTest test = null;
        try (Continuum c = builder.open()){
            test = new LoadTest(c);
            test.load(c, iterations);
        } finally {
            if (test != null) test.stop();
            if (delete) ref.delete();
        }
    }
}
