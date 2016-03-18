package continuum.core.atom;


import continuum.atom.Fields;

import java.util.HashMap;
import java.util.Map;

/**
 * Base Fields implementation
 */
public class NFields extends HashMap<String, Object> implements Fields {

    NFields() { super(); }

    public NFields(Map<String, Object> raw) {
        super(raw);
    }

    @Override
    public String put(String key, Object value) {
        return (String)super.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return super.get(key);
    }
}
