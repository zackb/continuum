package com.dlvr.continuum.series.db.impl;

import com.dlvr.continuum.series.db.Slab;
import org.rocksdb.*;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.dlvr.continuum.util.Bytes.*;

/**
 * Slab implemented with RocksDB
 * Created by zack on 2/10/16.
 */
public class RockSlab implements Slab {

    private RocksDB rock;
    private final String dataDir;
    public final String name;
    private boolean closed;

    public RockSlab(String name, String dataDir) throws Exception {
        this.name = name;
        this.dataDir = dataDir;
        RocksDB.loadLibrary();
        open();
    }

    public void open() throws Exception {
        if (!Files.exists(Paths.get(dataDir))) {
            Files.createDirectories(Paths.get(dataDir));
        }

        this.rock = RocksDB.open(createOptions(), dataDir);
        closed = false;
    }

    @Override
    public byte[] get(String key) throws Exception {
        return rock.get(bytes(key));
    }

    @Override
    public void put(String key, byte[] value) throws Exception {
        rock.put(bytes(key), value);
    }

    @Override
    public void merge(String key, byte[] value) throws Exception {
        rock.merge(bytes(key), value);
    }

    @Override
    public void close() {
        if (rock != null) {
            rock.close();
        }
        closed = true;
    }

    private static Options createOptions() {
        int cpus = Math.min(Runtime.getRuntime().availableProcessors(), 12);
        Options options = new Options().setCreateIfMissing(true)
                .setIncreaseParallelism(4)
                .setCompactionStyle(CompactionStyle.LEVEL)
                .setCompressionType(CompressionType.SNAPPY_COMPRESSION)
                .setMaxOpenFiles(-1)
                .setMaxBackgroundCompactions(4)
                .setMaxBackgroundFlushes(1)
                .createStatistics();

        options.getEnv().setBackgroundThreads(cpus, Env.COMPACTION_POOL);
        options.getEnv().setBackgroundThreads(1, Env.FLUSH_POOL);
        options.setMergeOperatorName("stringappend"); // 'uint64add'
        //options.createMissingColumnFamilies = true

        return options;
    }
}
