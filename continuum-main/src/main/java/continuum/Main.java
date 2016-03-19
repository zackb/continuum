package continuum;


import continuum.file.Reference;
import continuum.main.REPL;
import continuum.universe.Universe;

/**
 * Continuum CLI
 * Created by zack on 3/14/16.
 */
public class Main {

    private final Continuum continuum;

    public static void main(String[] args) throws Exception {
        new Main(args);
    }

    public Main(String[] args) throws Exception {
        String path = "universe.meta";
        Universe universe = Universe.bigBang(path);
        continuum = Continuum.continuum()
                .name(universe.name())
                .dimension(universe.dimension())
                .base((Reference[]) universe.hot())
                .open();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        new REPL(continuum).run();
        stop();
    }

    public void stop() {
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() throws Exception {
        if (continuum != null) continuum.close();
    }
}
