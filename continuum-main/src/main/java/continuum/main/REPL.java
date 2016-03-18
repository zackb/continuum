package continuum.main;

import continuum.Continuum;
import continuum.slice.Slice;
import continuum.util.JSON;
import continuum.util.datetime.Interval;
import static continuum.util.datetime.Util.*;

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
            console.writer().println("      <writes count>          number of writes for load test");
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
            String prefix = args[0];
            Interval end = Interval.valueOf(hoursAgo(1)),
                     start = Interval.valueOf(now()),
                     interval = null;

            if (args.length > 1)
                end = Interval.valueOf(args[1]);

            if (args.length > 2)
                interval = Interval.valueOf(args[2]);

            if (args.length > 3)
                start = Interval.valueOf(args[3]);

            Slice slice = continuum.slice(
                            continuum.scan("series1")
                                    .start(start)
                                    .end(end)
                                    .interval(interval)
                                    .build());
            console.writer().println(JSON.pretty(slice));
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

        boolean execute(String[] args) throws Exception {
            iterations = Integer.parseInt(args[0]);
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