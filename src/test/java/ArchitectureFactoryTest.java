import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArchitectureFactoryTest {

    private PDocument pDocument;

    @BeforeEach
    void setUp() {
        pDocument = new PDocument("pdfDocument.xml");
    }

    @Test
    void computeArchitecture() {

        pDocument = DuplicateFactory.INSTANCE.create(pDocument).removeIdenticalNodes().build();

        ArchitectureFactory.INSTANCE.create(pDocument).computeArchitecture();
    }
}