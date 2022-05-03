package cz.isdoc.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class Create {

    public static void main(String args[]) {
        System.out.println("ISDOC.PDF Creator");

        String pdfFilename = "examples/input/ABRAGen_FV-1_2021-a1.pdf";
        String isdocFilename = "examples/input/ABRAGx_ISDOC_FV-1-2021.isdoc";
        String outputFilename = "examples/output/test.pdf";

        PDDocument doc;

        try {
            System.out.println("Loading PDF file " + pdfFilename + "...");
            doc = PDDocument.load(new File(pdfFilename));
            //System.out.println("Pages: " + doc.getNumberOfPages());


            System.out.println("Writing merged file " + outputFilename + "...");
            doc.save(new File(outputFilename));
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
