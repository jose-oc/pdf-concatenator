package com.joseoc.pdf.concatenator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PdfMerger {

    private final List<URL> inputPdf;
    private final String outputPdf;

    public PdfMerger(List<URL> inputPdf, String outputPdf) {
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
}
