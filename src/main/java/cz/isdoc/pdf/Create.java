package cz.isdoc.pdf;

import org.apache.jempbox.xmp.XMPMetadata;
import org.apache.jempbox.xmp.XMPSchemaBasic;
import org.apache.jempbox.xmp.XMPSchemaPDF;
import org.apache.jempbox.xmp.pdfa.XMPSchemaPDFAId;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDMarkInfo;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class Create {

    public static void main(String args[]) {
        System.out.println("ISDOC.PDF Creator");

        if (args.length != 3) {
            System.out.println("Usage: java -jar isdoc-pdf.jar <input.pdf> <input.isdoc> <merged.pdf>");
        }
        else {
            String pdfFilename = args[0];
            String isdocFilename = args[1];
            String outputFilename = args[2];

            try {
                PDDocument doc;

                System.out.println("Loading PDF file " + pdfFilename + "...");
                doc = PDDocument.load(new File(pdfFilename));

                byte[] data = Files.readAllBytes(Paths.get(isdocFilename));

                PDComplexFileSpecification fs = new PDComplexFileSpecification();
                fs.setFile(isdocFilename);

                COSDictionary dict = fs.getCOSObject();
                dict.setName("AFRelationship", "Alternative");
                dict.setString("UF", "invoice.isdoc");
                //dict.setString("Desc", description);

                ByteArrayInputStream fakeFile = new ByteArrayInputStream(data);
                PDEmbeddedFile ef = new PDEmbeddedFile(doc, fakeFile);
                //		ef.addCompression();
                ef.setSubtype("text/xml");
                ef.setSize(data.length);
                ef.setCreationDate(new GregorianCalendar());

                ef.setModDate(GregorianCalendar.getInstance());

                fs.setEmbeddedFile(ef);

                // In addition make sure the embedded file is set under /UF
                dict = fs.getCOSObject();
                COSDictionary efDict = (COSDictionary) dict.getDictionaryObject(COSName.EF);
                COSBase lowerLevelFile = efDict.getItem(COSName.F);
                efDict.setItem(COSName.UF, lowerLevelFile);

                // now add the entry to the embedded file tree and set in the document.
                PDDocumentNameDictionary names = new PDDocumentNameDictionary(doc.getDocumentCatalog());
                PDEmbeddedFilesNameTreeNode efTree = names.getEmbeddedFiles();
                if (efTree == null) {
                    efTree = new PDEmbeddedFilesNameTreeNode();
                }

                Map<String, PDComplexFileSpecification> namesMap = new HashMap<>();

                Map<String, PDComplexFileSpecification> oldNamesMap = efTree.getNames();
                if (oldNamesMap != null) {
                    for (String key : oldNamesMap.keySet()) {
                        namesMap.put(key, oldNamesMap.get(key));
                    }
                }
                namesMap.put("invoice.isdoc", fs);
                efTree.setNames(namesMap);

                names.setEmbeddedFiles(efTree);
                doc.getDocumentCatalog().setNames(names);

                // AF entry (Array) in catalog with the FileSpec
                COSBase AFEntry = (COSBase) doc.getDocumentCatalog().getCOSObject().getItem("AF");
                if ((AFEntry == null)) {
                    COSArray cosArray = new COSArray();
                    cosArray.add(fs);
                    doc.getDocumentCatalog().getCOSObject().setItem("AF", cosArray);
                } else if (AFEntry instanceof COSArray) {
                    COSArray cosArray = (COSArray) AFEntry;
                    cosArray.add(fs);
                    doc.getDocumentCatalog().getCOSObject().setItem("AF", cosArray);
                } else if ((AFEntry instanceof COSObject) &&
                        ((COSObject) AFEntry).getObject() instanceof COSArray) {
                    COSArray cosArray = (COSArray) ((COSObject) AFEntry).getObject();
                    cosArray.add(fs);
                } else {
                    throw new IOException("Unexpected object type for PDFDocument/Catalog/COSDictionary/Item(AF)");
                }

                PDDocumentCatalog cat = doc.getDocumentCatalog();
                cat.setStructureTreeRoot(new PDStructureTreeRoot());
                PDMetadata metadata = new PDMetadata(doc);
                cat.setMetadata(metadata);
                XMPMetadata xmp = new XMPMetadata();
                XMPSchemaPDFAId pdfaid = new XMPSchemaPDFAId(xmp);
                xmp.addSchema(pdfaid);
                PDMarkInfo markinfo = new PDMarkInfo();
                markinfo.setMarked(true);
                cat.setMarkInfo(markinfo);
                pdfaid.setPart(3);
                pdfaid.setConformance("A");
                pdfaid.setAbout("");
                metadata.importXMPMetadata(xmp.asByteArray());
                cat.setMetadata(metadata);

                System.out.println("Writing merged file " + outputFilename + "...");
                doc.save(new File(outputFilename));
                System.out.println("Done");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
