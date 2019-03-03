import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        PDDocument document = new PDDocument();

        try {
            document.save("output.pdf");
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
