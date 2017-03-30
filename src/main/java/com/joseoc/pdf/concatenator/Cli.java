package com.joseoc.pdf.concatenator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
final class Cli {

    private final String[] args;
    private final Options options = new Options();

    public Cli(String[] args) {
        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("v", "version", false, "show current version.");

        options.addOption(Option.builder("i")
                .longOpt("inputs")
                .hasArg()
                .argName("input-file")
                .required(true)
                .desc("Input PDF files to be concatenated")
                .build()
        );

        options.addOption(Option.builder("o")
                .longOpt("output")
                .hasArg()
                .argName("output-file")
                .required(false)
                .desc("Output PDF file. In case this argument is omited a random file name will be generated")
                .build()
        );
    }


    public Params parse() {
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = null;
            cmd = parser.parse(options, args);
            return extractParamsFromCommandLine(cmd);
        } catch (ParseException e) {
            log.warn("Failed to parse comand line properties", e);
            help();
            throw new RuntimeException("Error parsing command line parameters. Parameters: " + args, e);
        }
    }

    private Params extractParamsFromCommandLine(CommandLine cmd) {
        Params params = new Params();
        if (cmd.hasOption("h"))
            help();

        if (cmd.hasOption("v"))
            version();

        if (cmd.hasOption("i")) {
            final String inputs = cmd.getOptionValue("i");
            log.debug("Using cli argument -i=" + inputs);
            params.setInputs(Arrays.stream(inputs.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList()));
        } else {
            log.error("MIssing i option");
            help();
        }

        if (cmd.hasOption("o")) {
            final String output = cmd.getOptionValue("o");
            log.debug("Using cli argument -o=" + output);
            params.setOutput(output.trim());
        }
        return params;
    }

    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();

        formater.printHelp("java -jar pdf-concatenator-1.0-SNAPSHOT.jar -i <input.pdf>,<input.pdf> -o <output.pdf>", options);
        System.exit(0);
    }

    private void version() {
        // TODO get maven app version
        System.out.println("version 0.1");
        System.exit(0);
    }
}
