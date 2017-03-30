package com.joseoc.pdf.concatenator;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PdfMergerTest {

    static List<URL> pdfFiles = new ArrayList<>(Arrays.asList(
            PdfMerger.class.getResource("/sample10.pdf"),
            PdfMerger.class.getResource("/sample20.pdf"),
            PdfMerger.class.getResource("/sample30.pdf"),
            PdfMerger.class.getResource("/sample40.pdf")
    ));

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void merge() throws Exception {
        final URL expected = PdfMerger.class.getResource("/sample-output.pdf");
        final File actualOutput = testFolder.newFile("output.pdf");

        PdfMerger pdfMerger = PdfMerger.builder()
                .inputPdfsToConcatenate(pdfFiles)
                .ontoThisOutputFile(actualOutput.getAbsolutePath())
                .build();
        pdfMerger.merge();

        assertEquals(Files.size(Paths.get(expected.toURI())), Files.size(actualOutput.toPath()));
    }

    @Test
    public void whenNoOutputGiven_thenGenerateDefaultOutputFilename() throws Exception {
        PdfMerger pdfMerger = PdfMerger.builder()
                .inputPdfsToConcatenate(pdfFiles)
                .build();
        String output = pdfMerger.getOutputPdf();

        assertNotNull(output);
    }

}