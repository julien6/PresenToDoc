import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateFactoryTest {

    private PDocument pDocument;

    @BeforeEach
    void setUp() {

    }

    @Test
    void removeIdenticalNodes() {

        pDocument = new PDocument("pdfDocument.xml");
        //pDocument = DuplicateFactory.INSTANCE.create(pDocument).removeIdenticalNodes().build();

        System.out.println(pDocument);
    }

}