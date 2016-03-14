package com.dlvr.continuum;

import com.dlvr.continuum.main.LoadTest;
import com.dlvr.continuum.universe.Universe;

import java.util.stream.Stream;

/**
 * Continuum CLI
 * Created by zack on 3/14/16.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        new Main(args).run();
    }

    private Continuum continuum;
    private final Command command;
    private int iterations = 2000;
    private String dataDir = "/tmp/LOAD-tmp";

    private enum Command {
        HELP,
        HELLO,
        REPL,
        LOAD;

        static Command from(String s) {
            return Stream.of(values())
                    .filter(c -> c.name().equalsIgnoreCase(s))
                    .findFirst()
                    .orElse(HELP);
        }
    }

    public Main(String[] args) throws Exception {
        command = args.length > 0 ? Command.from(args[0]) : Command.HELP;

        for (int i = 1; i < args.length; i++)
            if (args[i].equalsIgnoreCase("-w"))
                iterations = Integer.parseInt(args[i++]);
            else if (args[i].equalsIgnoreCase("-d"))
                dataDir = args[i++];
    }

    private void run() throws Exception {
        switch (command) {
            case HELLO:
                hello();
                break;
            case REPL:
                repl();
                break;
            case LOAD:
                LoadTest.load(dataDir, iterations, true);
                break;
            case HELP:
            default:
                usage();
        }
    }

    private void repl() throws Exception {
        open();
        close();
    }

    private void close() throws Exception {
        continuum.close();
    }

    private void open() throws Exception {
        String path = "universe.meta";
        Universe universe = Universe.bigBang(path);
        continuum = Continuum.continuum()
                .name(universe.name())
                .base(universe.hot())
                .dimension(universe.dimension())
                .open();
    }

    private void hello() throws Exception {
        open();
        Continuum.sayHello();
        close();
    }

    private static void usage() {
        System.err.println("");
        System.err.println("Continuum CLI Usage: ");
        System.err.println("continuum <command> <options> <arg...>");
        System.err.println("    help                    show this message");
        System.err.println("    hello                   say hello verifying config");
        System.err.println("    load                    load test");
        System.err.println("            -w <writes per> number of writes for load test");
        System.err.println("            -d <data dir>   directory to use for load data");
    }
}
