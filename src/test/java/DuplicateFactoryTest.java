import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateFactoryTest {

    private PDocument pDocument;

    @BeforeEach
    void setUp() {
        pDocument = new PDocument("pdfDocument.xml");
    }

    @Test
    void removeIdenticalNodes() {

        pDocument = DuplicateFactory.INSTANCE.create(pDocument).removeIdenticalNodes().build();

        System.out.println(pDocument);
    }

}