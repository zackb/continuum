package continuum.file;

import java.util.ArrayList;
import java.util.List;

/**
 * An ordered collection of block based storage devices
 * Created by zack on 2/15/16.
 */
public class References {

    private final List<Reference> references = new ArrayList<>(4);

    public References(List<Reference> refs) {
        this.references.addAll(refs);
    }
}
