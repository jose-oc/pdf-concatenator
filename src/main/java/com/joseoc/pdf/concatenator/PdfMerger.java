package com.joseoc.pdf.concatenator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
public final class PdfMerger {

    private final List<URL> inputPdf;
    private final String outputPdf;

    public static PdfMergerBuilder builder() {
        return new PdfMergerBuilder();
    }

    private PdfMerger(List<URL> inputPdf, String outputPdf) {
        this.inputPdf = inputPdf;
        this.outputPdf = outputPdf;
    }

    public void merge() throws IOException, DocumentException {
        Document document = new Document();
        // you can also user new PdfSmartCopy(...) http://stackoverflow.com/questions/23062345/function-that-can-use-itext-to-concatenate-merge-pdfs-together-causing-some
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(outputPdf));

        document.open();
        for (URL file : inputPdf){
            PdfReader reader = new PdfReader(file);
            copy.addDocument(reader);
            copy.freeReader(reader);
            reader.close();
        }
        document.close();
    }


    @ToString
    public static final class PdfMergerBuilder {
        private List<URL> inputsPdf;
        private String outputPdf;

        protected PdfMergerBuilder() {}

        public PdfMergerBuilder inputPdfsToConcatenate(List<URL> inputsPdf) {
            this.inputsPdf = inputsPdf;
            return this;
        }

        public PdfMergerBuilder ontoThisOutputFile(String output) {
            this.outputPdf = output;
            return this;
        }

        public PdfMerger build() {
            if (empty(inputsPdf)) {
                throw new IllegalArgumentException("You have to provide at least one PDF file as input");
            }

            if (isBlank(outputPdf)) {
                outputPdf = "output_" + now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS")) + ".pdf";
            }
            return new PdfMerger(inputsPdf, outputPdf);
        }

        private boolean empty(List<URL> inputsPdf) {
            return Objects.isNull(inputsPdf) || (inputsPdf.stream().filter(Objects::nonNull).count() == 0);
        }
    }
}
