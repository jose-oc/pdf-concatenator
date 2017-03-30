package com.joseoc.pdf.concatenator;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public final class Main {

    public static void main(String[] args) {
        String message = "PDF-concatenator executed at " + LocalDateTime.now();
        log.debug(message);

        Params params = parseParams(args);
        System.out.println("Inputs: " + params.getInputs() + "\nOutput: " + params.getOutput());

        try {
            List<URL> inputs = params.getInputs().stream()
                    .map(i -> string2Uri(i))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            PdfMerger pdfMerger = new PdfMerger(inputs, params.getOutput());
            pdfMerger.merge();
        } catch (Exception e) {
            log.error("Error concatenating PDFs. Params: \n\t" + params, e);
        }

        System.out.println("PDF generated: " + Files.exists(Paths.get(params.getOutput())));
    }

    private static Params parseParams(String[] args) {
        try {
            return new Cli(args).parse();
        } catch (Exception e) {
            log.error("Error parsing params", e);
            return null;
        }
    }

    private static URL string2Uri(String fileUri) {
        try {
            return Paths.get(fileUri).toUri().toURL();
        } catch (Exception e) {
            log.warn("Error converting to URI: " + fileUri, e);
            return null;
        }
    }

}
