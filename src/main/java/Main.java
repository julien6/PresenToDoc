import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;

// https://www.tutorialspoint.com/pdfbox/pdfbox_document_properties.htm

// TODO : Find main goals of PresenToDoc :
//  - save, load,
//  - reorganize information
//      - extract text,
//      - extract picture
//      - several choices of organisation (text and pictures two sided, find ways to keep main organisation)
//          - extract structural and comestical informations

public class Main {

    public static void main(String[] args) {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        File file = new File("pdfDocument.pdf");

        System.out.println("ok");
        System.out.println("ok");

        try {
            document.save("output.pdf");
            document.addPage(page);
            PDDocument doc2 = PDDocument.load(file);
            doc2.save("second.pdf");
            doc2.close();
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
