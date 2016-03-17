package com.dlvr.continuum.main;

import com.dlvr.continuum.Continuum;

import java.io.*;

/**
 * Read Eval Print Loop
 *
 * Created by zack on 3/16/16.
 */
public class REPL {

    private static final String prompt = "continuum> ";

    private final Continuum continuum;
    private final Console console;

    public REPL(Continuum continuum) {
        this.continuum = continuum;
        this.console = System.console();
    }

    private boolean process(String command) throws IOException {
        if (command == null) return false;
        switch (command) {
            case "help":
            default:
                usage();
                break;
        }
        return true;
    }

    public void run() throws IOException {
        String command;
        do {
            console.printf("%s", prompt);
            console.flush();
            command = console.readLine();
        } while (process(command));

        stop();
    }

    public void stop() { }

    private void usage() throws IOException {
        console.writer().println("Commands:");
        console.writer().println("  help                        show command help");
        console.writer().println("  scan                        return a slice of the continuum");
        console.writer().println("      name                    series name or time-key");
        console.writer().println("      <end>                   end timestamp in millisecond epoch");
        console.writer().println("      <start>                 start timestamp in millisecond epoch");
        console.writer().println("  write                       write an atom to the continuum");
        console.writer().println("      name                    series name or time-key");
        console.writer().println("      <timestamp>             atom timestamp in millisecond epoch");
        console.writer().println("      <particles>");
        console.writer().println("      <fields>");
        console.writer().println("      <values>");
        console.writer().println("");
        console.writer().flush();
    }
}
