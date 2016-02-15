package com.dlvr.continuum.universe;

import com.dlvr.continuum.core.db.RockSlab;
import com.dlvr.continuum.core.io.file.FileSystemReference;
import com.dlvr.continuum.db.Slab;
import com.dlvr.continuum.io.file.Reference;
import com.dlvr.util.IOUtil;
import com.dlvr.util.JSON;

import java.util.Map;

/**
 * What people of Earth call "Config"
 * Created by zack on 2/15/16.
 */
public class Universe {

    private final double version;

    /**
     * Create a universe
     * @param ref location of universe.meta on the filestystem or other storage
     * @return a universe comensurate with universe.meta
     */
    public static Universe bigBang(Reference ref) throws Exception {
        String string = IOUtil.readString(ref.getInputStream());
        Map<String, Object> meta = JSON.decode(string);
        Universe u = new Universe(0.01);
        return u;
    }

    public Universe(Map<String, Object> meta) {
        this.version = (double)meta.get("version");
    }

    public double getVersion() {
        return version;
    }

    public final Slab getHotSlab() {
        return null;
    }

    public final Slab getColdSlab() throws Exception {
        return null;
    }

    public final Slab getGlacierSlab() throws Exception {
        return new RockSlab("glacier", new FileSystemReference("/dev/null"));
    }
}
