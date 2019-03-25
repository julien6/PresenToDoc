import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArchitectureFactoryTest {

    private PDocument pDocument;

    @BeforeEach
    void setUp() {
        pDocument = new PDocument("pdfDocument.xml");
    }

    @Test
    void computeArchitecture() {

        pDocument = DuplicateFactory.INSTANCE.create(pDocument).removeIdenticalNodes().build();

        pDocument = ArchitectureFactory.INSTANCE.create(pDocument, 3).formating().build();

        System.out.println(pDocument);
    }
}