package continuum.main;

import continuum.Continuum;
import continuum.slice.Slice;
import continuum.util.JSON;
import continuum.util.datetime.Interval;
import static continuum.util.datetime.Util.*;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Read Eval Print Loop
 *
 * Created by zack on 3/16/16.
 */
public class REPL {

    private static final String prompt = "continuum> ";

    private Continuum continuum;
    private final PrintWriter stdout;
    private final Scanner stdin;

    public REPL(Continuum continuum) {
        this.continuum = continuum;
        this.stdout = new PrintWriter(System.out);
        this.stdin = new Scanner(System.in);
    }

    public void run() throws Exception {
        do {
            stdout.printf("%s", prompt);
            stdout.flush();
        } while (execute(stdin.nextLine()));
    }

    private boolean execute(String input) throws Exception {
        String[] parts = input.split(" ");
        if (parts.length < 1) return HELP.execute(parts);
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
            stdout.println("Commands:");
            stdout.println(" help                         show command help");
            stdout.println(" scan                         return a slice of the continuum");
            stdout.println("      name                    series name or time-key");
            stdout.println("      <end>                   end timestamp in millisecond epoch");
            stdout.println("      <start>                 start timestamp in millisecond epoch");
            stdout.println(" write                        write an atom to the continuum");
            stdout.println("      name                    series name or time-key");
            stdout.println("      <timestamp>             atom timestamp in millisecond epoch");
            stdout.println("      <particles>");
            stdout.println("      <fields>");
            stdout.println("      <values>");
            stdout.println(" load                         load test");
            stdout.println("      <writes count>          number of writes for load test");
            stdout.println("");
            stdout.flush();
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
            stdout.printf("\033[H\033[2J");
            stdout.flush();
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
            Interval end = Interval.valueOf("1h"),
                     start = Interval.valueOf(0),
                     interval = null;

            if (args.length > 1)
                end = Interval.valueOf(args[1]);

            if (args.length > 2)
                interval = Interval.valueOf(args[2]);

            if (args.length > 3)
                start = Interval.valueOf(args[3]);

            Slice slice = continuum.slice(
                            continuum.scan(prefix)
                                    .start(start)
                                    .end(end)
                                    .interval(interval)
                                    .build());
            stdout.println(JSON.pretty(slice));
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
        // write key value <timestamp>
        boolean execute(String[] args) throws Exception {
            if (args.length < 2) return HELP.execute(args);

            String key = args[0];
            Double value = Double.parseDouble(args[1]);
            Long timestamp = System.currentTimeMillis();
            if (args.length > 2)
                timestamp = Long.parseLong(args[2]);

            continuum.write(
                    continuum.atom()
                        .name(key)
                        .particles("k", "v")
                        .value(value)
                        .timestamp(timestamp)
                        .build());
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
