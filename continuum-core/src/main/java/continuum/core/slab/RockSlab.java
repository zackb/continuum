package continuum.core.slab;

import continuum.core.io.file.FileSystemReference;
import continuum.slab.Slab;
import continuum.file.Reference;
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

    static {
        RocksDB.loadLibrary();
    }

    /**
     * {@inheritDoc}
     */
    public RockSlab(String name, FileSystemReference dataDirRef) {
        this.name = name;
        this.dataDirRef = dataDirRef.child(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() throws Exception {
        dataDirRef.mkdir();
        rock = RocksDB.open(createOptions(), dataDirRef.fullPath());
        closed = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] read(byte[] key) throws Exception {
        return rock.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] key, byte[] value) throws Exception {
        rock.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(byte[] key, byte[] value) throws Exception {
        rock.merge(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        if (rock != null) {
            rock.close();
        }
        closed = true;
    }

    RocksDB rockdDB() {
        return this.rock;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reference reference() {
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