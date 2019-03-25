import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExportFactoryTest {

    private PDocument pDocument;

    @BeforeEach
    void setUp() {
        pDocument = new PDocument("pdfDocument.xml");
    }


    @Test
    void convertToLatex() {
        pDocument = DuplicateFactory.INSTANCE.create(pDocument).removeIdenticalNodes().build();

        pDocument = ArchitectureFactory.INSTANCE.create(pDocument, 3).formating().build();

        String res = ExportFactory.INSTANCE.create(pDocument).convertToLatex("test.latex");

        System.out.println(res);
    }
}