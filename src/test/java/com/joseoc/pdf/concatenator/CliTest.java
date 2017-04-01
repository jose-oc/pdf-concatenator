package com.joseoc.pdf.concatenator;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CliTest {

    @Test
    public void parse() throws Exception {
        String[] args = new String[] {"-i", "file1.pdf", "-i", "file2.pdf", "-i", "file3.pdf", "-o", "output.pdf"};
        Cli cli = new Cli(args);
        Params params = cli.parse();

        System.out.println(params);

        assertEquals(Arrays.asList("file1.pdf", "file2.pdf", "file3.pdf"), params.getInputs());
        assertEquals("output.pdf", params.getOutput());
    }

    @Test
    public void parseSpaces() throws Exception {
        String[] args = new String[] {"-i", "file1.pdf", "file2.pdf", "file3.pdf", "-o", "output.pdf"};
        Cli cli = new Cli(args);
        Params params = cli.parse();

        System.out.println(params);

        assertEquals(Arrays.asList("file1.pdf", "file2.pdf", "file3.pdf"), params.getInputs());
        assertEquals("output.pdf", params.getOutput());
    }
}