package continuum.core.slab;


import continuum.Continuum;

/**
 * Continuously stream new atoms written to slabs
 * Created by zack on 3/18/16.
 */
public class TailingIterator extends AtomIterator {
    public TailingIterator(Continuum.Dimension dimension, RockSlab slab) {
        super(dimension, slab, true);
    }
}
