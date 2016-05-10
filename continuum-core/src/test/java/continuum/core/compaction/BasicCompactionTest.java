package continuum.core.compaction;

import continuum.Continuum;
import continuum.atom.Atom;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zack on 5/9/16.
 */
public class BasicCompactionTest {

    @Test
    public void testMatch() {
        Atom a1 = Continuum.satom().name("foo").value(111.1d).build();
        Atom a2 = Continuum.satom().name("foo").value(1.8d).build();

        assert BasicCompaction.match(a1, a2);

        a1 = Continuum.satom().name("foo1").value(111.1d).build();
        a2 = Continuum.satom().name("foo").value(111.1d).build();

        assert !BasicCompaction.match(a1, a2);

        a1 = Continuum.satom().name("foo").particles("fee", "bar").value(111.1d).build();
        a2 = Continuum.satom().name("foo").particles("fee", "bar").value(1.1d).build();

        assert BasicCompaction.match(a1, a2);

        a1 = Continuum.satom().name("foo").particles("fee", "ar").value(111.1d).build();
        a2 = Continuum.satom().name("foo").particles("fee", "bar").value(1.1d).build();

        assert !BasicCompaction.match(a1, a2);

        a1 = Continuum.satom().name("foo").particles("fee", "bar", "z", "b").value(111.1d).build();
        a2 = Continuum.satom().name("foo").particles("fee", "bar").value(1.1d).build();

        assert !BasicCompaction.match(a1, a2);

        a1 = Continuum.satom().name("foo").particles("fee", "bar", "z", "b").value(111.1d).build();
        a2 = Continuum.satom().name("foo").particles("fee", "bar", "z", "ddddd").value(1.1d).build();

        assert !BasicCompaction.match(a1, a2);

        a1 = Continuum.satom().name("foo").particles("fee", "bar", "z", "b", "one", "two").value(111.1d).build();
        a2 = Continuum.satom().name("foo").particles("fee", "bar", "z", "b", "one", "two").value(1.1d).build();

        assert BasicCompaction.match(a1, a2);
    }
}