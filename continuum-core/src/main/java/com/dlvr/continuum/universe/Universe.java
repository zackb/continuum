package com.dlvr.continuum.universe;

import com.dlvr.continuum.core.db.RockSlab;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.db.Slab;
import com.dlvr.continuum.io.file.Reference;
import com.dlvr.util.IOUtil;
import com.dlvr.continuum.util.JSON;

import java.util.Map;

/**
 * Config
 * Created by zack on 2/15/16.
 */
public class Universe {

    private final double version;

    private final Slab hotSlab;
    private final Slab coldSlab;

    private final Map<String, Object> meta;

    /**
     * Create a universe
     * @param ref config location of universe.meta on the filestystem or other storage
     * @return a universe comensurate with universe.meta
     */
    public static Universe bigBang(Reference ref) throws Exception {
        String string = IOUtil.readString(ref.inputStream());
        Map<String, Object> meta = JSON.decode(string);
        return new Universe(meta);
    }

    public Universe(Map<String, Object> meta) throws Exception {
        this.meta = meta;
        this.version = (double)meta.get("version");
    }

    public Integer integer(String name) {
        int res = 0;
        Object foo = meta.get(name);
        if (foo != null) {
            if (foo instanceof Number) {
                res = (int) foo;
            } else {
                res = Integer.parseInt((String) foo);
            }
        }
        return res;
    }

    public double version() {
        return version;
    }

    public final Slab hot() {
        return null;
    }

    public final Slab cold() throws Exception {
        return null;
    }

    public final Slab glacier() throws Exception {
        return new RockSlab("glacier", new FileSystemReference("/dev/null"));
    }
}
