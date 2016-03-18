package continuum.control;

import continuum.Continuum;
import continuum.file.Reference;

/**
 * Build a continuum
 *
 * Created by zack on 3/16/16.
 */
public interface Builder<T> {
    Builder<T> name(String name);
    Builder<T> base(Reference base);
    Builder<T> base(Reference... bases);
    Builder<T> dimension(Continuum.Dimension dimension);
    Controller open() throws Exception;
}
