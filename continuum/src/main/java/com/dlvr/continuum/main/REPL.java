package com.dlvr.continuum.main;

import com.dlvr.continuum.Continuum;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Read Eval Print Loop
 *
 * Created by zack on 3/16/16.
 */
public class REPL {

    private static final String prompt = "continuum> ";

    private Continuum continuum;
    private final Console console;
    private boolean run = true;

    public REPL(Continuum continuum) {
        this.continuum = continuum;
        this.console = System.console();
    }

    public void run() throws Exception {
        do {
            console.printf("%s", prompt);
            console.flush();
        } while (execute(console.readLine()));
    }

    private boolean execute(String input) throws Exception {
        String[] parts = input.split(" ");
        Command command = from(parts[0].toLowerCase());
        return command.execute(
            Arrays.asList(parts)
                    .subList(1, parts.length)
                    .stream()
                    .toArray(String[]::new)
        );
    }

    private abstract class Command {
        abstract boolean execute(String[] args) throws Exception;
        abstract boolean accepts(String input);
    }

    private Command from(String input) {
        return Stream.of(COMMANDS)
                .filter(c -> c.accepts(input))
                .findFirst()
                .orElse(HELP);
    }

    private final Command HELP = new Command() {
        @Override
        boolean execute(String[] args) throws Exception {
            console.writer().println("Commands:");
            console.writer().println(" help                         show command help");
            console.writer().println(" scan                         return a slice of the continuum");
            console.writer().println("      name                    series name or time-key");
            console.writer().println("      <end>                   end timestamp in millisecond epoch");
            console.writer().println("      <start>                 start timestamp in millisecond epoch");
            console.writer().println(" write                        write an atom to the continuum");
            console.writer().println("      name                    series name or time-key");
            console.writer().println("      <timestamp>             atom timestamp in millisecond epoch");
            console.writer().println("      <particles>");
            console.writer().println("      <fields>");
            console.writer().println("      <values>");
            console.writer().println(" load                         load test");
            console.writer().println("   -w <writes count>          number of writes for load test");
            console.writer().println("   -d <data dir>              directory to use for load data");
            console.writer().println("");
            console.writer().flush();
            return true;
        }

        @Override
        boolean accepts(String input) {
            return input.startsWith("help") ||
                   input.equals("h")    ||
                   input.startsWith("?");
        }
    };

    private final Command HELLO = new Command() {
        @Override
        boolean execute(String[] args) throws Exception {
            Continuum.sayHello();
            return true;
        }

        @Override
        boolean accepts(String input) {
            return input.startsWith("hello");
        }
    };

    private final Command CLEAR = new Command() {
        boolean execute(String[] args) throws Exception {
            console.printf("\033[H\033[2J");
            console.flush();
            return true;
        }

        @Override
        boolean accepts(String input) {
            return (input.charAt(0) == 12 || input.startsWith("clear"));
        }
    };

    private final Command SCAN = new Command() {
        boolean execute(String[] args) throws Exception {
            return true;
        }

        @Override
        boolean accepts(String input) {
            return input.startsWith("scan");
        }
    };

    private final Command READ = new Command() {
        boolean execute(String[] args) throws Exception {
            return true;
        }

        @Override
        boolean accepts(String input) {
            return input.startsWith("read");
        }
    };

    private final Command WRITE = new Command() {
        boolean execute(String[] args) throws Exception {
            return true;
        }
        @Override
        boolean accepts(String input) {
            return input.startsWith("write");
        }
    };

    private final Command LOAD = new Command() {

        private int iterations = 1000;
        private String dataDir = "/tmp/LOAD-tmp";

        boolean execute(String[] args) throws Exception {
            for (int i = 1; i < args.length; i++)
                if (args[i].equalsIgnoreCase("-w"))
                    iterations = Integer.parseInt(args[++i]);
                else if (args[i].equalsIgnoreCase("-d"))
                    dataDir = args[++i];
            LoadTest.perform(continuum, iterations);
            return true;
        }
        @Override
        boolean accepts(String input) {
            return input.startsWith("load");
        }
    };

    private final Command QUIT = new Command() {
        boolean execute(String[] args) throws Exception {
            return false;
        }
        @Override
        boolean accepts(String input) {
            return input.startsWith("quit") || input.equals("q");
        }
    };

    private final Command[] COMMANDS = new Command[] {
            HELP,QUIT,HELLO,CLEAR,LOAD,SCAN,READ,WRITE
    };
}
