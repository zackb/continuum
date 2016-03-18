package continuum.tree;

/**
 * Visitable
 * Created by zack on 3/12/16.
 */
public interface Visitable<V> {
    void visit(Visitor<V> visitor);
}
