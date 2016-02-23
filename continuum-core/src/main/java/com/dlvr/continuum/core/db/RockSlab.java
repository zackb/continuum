package com.dlvr.continuum.core.db;

import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.db.Slab;
import org.rocksdb.*;

/**
 * Slab implemented with RocksDB
 * Created by zack on 2/10/16.
 */
public class RockSlab implements Slab {

    private RocksDB rock;
    public final String name;
    private boolean closed;
    private FileSystemReference dataDirRef;

    public RockSlab(String name, FileSystemReference dataDirRef) throws Exception {
        this.name = name;
        this.dataDirRef = dataDirRef.child(name);
        RocksDB.loadLibrary();
        open();
    }

    public void open() throws Exception {
        dataDirRef.mkdir();
        rock = RocksDB.open(createOptions(), dataDirRef.fullPath());
        closed = false;
    }

    @Override
    public byte[] get(byte[] key) throws Exception {
        return rock.get(key);
    }

    @Override
    public void put(byte[] key, byte[] value) throws Exception {
        rock.put(key, value);
    }

    @Override
    public void merge(byte[] key, byte[] value) throws Exception {
        rock.merge(key, value);
    }

    RocksDB getRocksDB() {
        return this.rock;
    }

    @Override
    public void close() {
        if (rock != null) {
            rock.close();
        }
        closed = true;
    }

    public FileSystemReference getReference() {
        return dataDirRef;
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
