package com.dlvr.continuum.main;

import com.dlvr.continuum.Continuum;

import java.io.*;

/**
 * Read Eval Print Loop
 *
 * Created by zack on 3/16/16.
 */
public class REPL {

    private final Continuum continuum;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public REPL(Continuum continuum) {
        this.continuum = continuum;
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
    }

    private String read() throws IOException {
        return reader.readLine();
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
        for (
            String command = read();
            process(command);
            command = read()
        );

        stop();
    }

    public void stop() { }

    private void usage() throws IOException {
        writer.write("\nCommands:\n");
        writer.write("  help                        show command help\n");
        writer.write("  slice                       return a slice of the continuum\n");
        writer.write("      name                    series name or time-key\n");
        writer.write("      <end>                   end timestamp in millisecond epoch\n");
        writer.write("      <start>                 start timestamp in millisecond epoch\n");
        writer.write("  write                       write an atom to the continuum\n");
        writer.write("      name                    series name or time-key\n");
        writer.write("      <timestamp>             atom timestamp in millisecond epoch\n");
        writer.write("      <particles>\n");
        writer.write("      <fields>\n");
        writer.write("      <values>\n");
        writer.flush();
    }
}
