import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDocumentTest {

    private PDocument pDocument;

    @BeforeEach
    void setUp() {
        pDocument = new PDocument("pdfDocument.xml");
    }

    @Test
    void getNbPages() {
        assertEquals(pDocument.getNbPages(), 15);
    }

    @Test
    void save() {
        pDocument.save("outputTest.xml");
    }

}