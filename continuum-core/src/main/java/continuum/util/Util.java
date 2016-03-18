package continuum.util;

/**
 * Random utilities that dont fit anywhere else
 * Created by zack on 2/12/16.
 */
public class Util {
    public static void checkNotNull(String name, Object value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException("Values: '" + name + "'must be set");
        }
    }
}
