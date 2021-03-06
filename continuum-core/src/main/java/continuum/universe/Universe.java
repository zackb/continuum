package continuum.universe;

import continuum.Continuum;
import continuum.core.io.file.FileSystemReference;
import continuum.file.Reference;
import continuum.util.IO;
import continuum.util.JSON;

import java.util.Map;

/**
 * Config
 * Created by zack on 2/15/16.
 */
public class Universe {

    private final double version;
    private final String name;
    private final Continuum.Dimension dimension;

    private final FileSystemReference[] hot;
    private final FileSystemReference cold;

    // default ttl for data points in seconds
    private final int ttl;

    private final Map<String, Object> meta;

    /**
     * Create a universe
     * @param filename config location of universe.meta on the filestystem or other storage
     * @return a universe comensurate with universe.meta
     * @throws Exception error opening universe config
     */
    public static Universe bigBang(String filename) throws Exception {
        String string = IO.readString(new FileSystemReference(filename).inputStream());
        Map<String, Object> meta = JSON.decode(string);
        return new Universe(meta);
    }

    /**
     * Create a universe
     * @param ref config location of universe.meta on the filestystem or other storage
     * @return a universe comensurate with universe.meta
     * @throws Exception error opening universe config
     */
    public static Universe bigBang(Reference ref) throws Exception {
        String string = IO.readString(ref.inputStream());
        Map<String, Object> meta = JSON.decode(string);
        return new Universe(meta);
    }

    public Universe(Map<String, Object> meta) throws Exception {
        this.meta = meta;
        this.version = (double)meta.get("version");
        this.name = (String)meta.get("name");
        this.dimension = Continuum.Dimension.valueOf((String)meta.get("dimension"));
        String hotRefs = (String)meta.get("hot");
        String[] parts = hotRefs.split(",");
        hot = new FileSystemReference[parts.length];
        Integer ttl = (Integer)meta.get("ttl");

        if (ttl != null)
            this.ttl = ttl;
        else this.ttl = 0;

        int i = 0;
        for (String sref : parts) hot[i++] = new FileSystemReference(sref);
        this.cold = null; // TODO:
    }

    public Integer integer(String name) {
        int res = 0;
        Object foo = meta.get(name);
        if (foo != null)
            if (foo instanceof Number)
                res = (int) foo;
            else
                res = Integer.parseInt((String) foo);

        return res;
    }

    public String string(String name) {
        return (String)meta.get(name);
    }

    public double version() {
        return version;
    }

    public final FileSystemReference[] hot() {
        return hot;
    }

    public final Reference cold() {
        return cold;
    }

    public final Reference glacier() throws Exception {
        return new FileSystemReference("/dev/null");
    }

    public final int ttl() {
        return ttl;
    }

    public final Continuum.Dimension dimension() {
        return dimension;
    }

    public final String name() {
        return name;
    }
}
